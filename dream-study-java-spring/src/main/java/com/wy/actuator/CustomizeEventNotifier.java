package com.wy.actuator;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractEventNotifier;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 自定义Admin的告警功能
 *
 * @author 飞花梦影
 * @date 2025-04-24 00:19:07
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
public class CustomizeEventNotifier extends AbstractEventNotifier {

	protected CustomizeEventNotifier(InstanceRepository repository) {
		super(repository);
	}

	@Override
	protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
		return Mono.fromRunnable(() -> {
			// 应用启动时,也会有状态改变
			if (event instanceof InstanceStatusChangedEvent) {
				log.info("Instance Status Change...");
			} else {
				log.info("Instane Info...:{},{},{}", instance.getRegistration().getName(), event.getInstance(),
						event.getType());
			}
		});
	}
}