package com.wy.service.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wy.service.ImageClassificationService;
import com.wy.utils.ModelUtils;

import ai.djl.MalformedModelException;
import ai.djl.Model;
import ai.djl.basicdataset.cv.classification.ImageFolder;
import ai.djl.inference.Predictor;
import ai.djl.metric.Metrics;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.ndarray.types.Shape;
import ai.djl.training.DefaultTrainingConfig;
import ai.djl.training.EasyTrain;
import ai.djl.training.Trainer;
import ai.djl.training.TrainingConfig;
import ai.djl.training.TrainingResult;
import ai.djl.training.dataset.RandomAccessDataset;
import ai.djl.training.evaluator.Accuracy;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.loss.Loss;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

/**
 * DJL图片训练服务
 * 
 * DJL是一个使用Java API简化模型训练、测试、部署和使用深度学习模型进行推理的开源库深度学习工具包,协议是Apache-2.0
 * 
 * 需要先下载训练集,之后解压,项目中添加DJL相关包
 * 
 * <pre>
 * wget https://vision.cs.utexas.edu/projects/finegrained/utzap50k/ut-zap50k-images-square.zip
 * 文档:https://docs.djl.ai/master/docs/demos/footwear_classification/index.html#train-the-footwear-classification-model
 * </pre>
 *
 * @author 飞花梦影
 * @date 2024-11-21 14:14:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
@Service
public class ImageClassificationServiceImpl implements ImageClassificationService {

	// represents number of training samples processed before the model is updated
	private static final int BATCH_SIZE = 32;

	// the number of passes over the complete dataset
	private static final int EPOCHS = 2;

	// the number of classification labels: boots, sandals, shoes, slippers
	@Value("${djl.num-of-output:4}")
	public int numOfOutput;

	@Override
	public String predict(MultipartFile image, String modePath)
			throws IOException, MalformedModelException, TranslateException {
		@Cleanup
		InputStream is = image.getInputStream();
		Path modelDir = Paths.get(modePath);
		BufferedImage bi = ImageIO.read(is);
		Image img = ImageFactory.getInstance().fromImage(bi);
		// empty model instance
		try (Model model = Model.newInstance(ModelUtils.MODEL_NAME)) {
			// load the model
			model.load(modelDir, ModelUtils.MODEL_NAME);
			// define a translator for pre and post processing
			// out of the box this translator converts images to ResNet friendly ResNet 18
			// shape
			Translator<Image,
					Classifications> translator = ImageClassificationTranslator.builder()
							.addTransform(new Resize(ModelUtils.IMAGE_WIDTH, ModelUtils.IMAGE_HEIGHT))
							.addTransform(new ToTensor())
							.optApplySoftmax(true)
							.build();
			// run the inference using a Predictor
			try (Predictor<Image, Classifications> predictor = model.newPredictor(translator)) {
				// holds the probability score per label
				Classifications predictResult = predictor.predict(img);
				log.info("reusult={}", predictResult.toJson());
				return predictResult.toJson();
			}
		}
	}

	@Override
	public String training(String datasetRoot, String modePath) throws TranslateException, IOException {
		log.info("Image dataset training started...Image dataset address path：{}", datasetRoot);
		// the location to save the model
		Path modelDir = Paths.get(modePath);
		// create ImageFolder dataset from directory
		ImageFolder dataset = initDataset(datasetRoot);
		// Split the dataset set into training dataset and validate dataset
		RandomAccessDataset[] datasets = dataset.randomSplit(8, 2);
		// set loss function, which seeks to minimize errors
		// loss function evaluates model's predictions against the correct answer
		// (during training)
		// higher numbers are bad - means model performed poorly; indicates more errors;
		// want to
		// minimize errors (loss)
		Loss loss = Loss.softmaxCrossEntropyLoss();
		// setting training parameters (ie hyperparameters)
		TrainingConfig config = setupTrainingConfig(loss);
		try (Model model = Model.newInstance(numOfOutput); // empty model instance to hold patterns
				Trainer trainer = model.newTrainer(config)) {
			// metrics collect and report key performance indicators, like accuracy
			trainer.setMetrics(new Metrics());
			Shape inputShape = new Shape(1, 3, ModelUtils.IMAGE_HEIGHT, ModelUtils.IMAGE_HEIGHT);
			// initialize trainer with proper input shape
			trainer.initialize(inputShape);
			// find the patterns in data
			EasyTrain.fit(trainer, EPOCHS, datasets[0], datasets[1]);
			// set model properties
			TrainingResult result = trainer.getTrainingResult();
			model.setProperty("Epoch", String.valueOf(EPOCHS));
			model.setProperty("Accuracy", String.format("%.5f", result.getValidateEvaluation("Accuracy")));
			model.setProperty("Loss", String.format("%.5f", result.getValidateLoss()));
			// save the model after done training for inference later
			// model saved as shoeclassifier-0000.params
			model.save(modelDir, ModelUtils.MODEL_NAME);
			// save labels into model directory
			Models.saveSynset(modelDir, dataset.getSynset());
			log.info("Image dataset training completed......");
			return String.join("\n", dataset.getSynset());
		}
	}

	private ImageFolder initDataset(String datasetRoot) throws IOException, TranslateException {
		ImageFolder dataset = ImageFolder.builder()
				// retrieve the data
				.setRepositoryPath(Paths.get(datasetRoot))
				.optMaxDepth(10)
				.addTransform(new Resize(ModelUtils.IMAGE_WIDTH, ModelUtils.IMAGE_HEIGHT))
				.addTransform(new ToTensor())
				// random sampling; don't process the data in order
				.setSampling(BATCH_SIZE, true)
				.build();
		dataset.prepare();
		return dataset;
	}

	private TrainingConfig setupTrainingConfig(Loss loss) {
		return new DefaultTrainingConfig(loss).addEvaluator(new Accuracy())
				.addTrainingListeners(TrainingListener.Defaults.logging());
	}
}