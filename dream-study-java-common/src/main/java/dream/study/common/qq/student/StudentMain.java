package dream.study.common.qq.student;

/**
 * 学生端启动
 * 
 * @author ParadiseWY
 * @date 2020-11-16 16:20:04
 * @git {@link https://github.com/mygodness100}
 */
public class StudentMain {

	public static void main(String[] args) {
		StudentUI ui = new StudentUI();
		new ReceiverThread(ui).start();
	}
}