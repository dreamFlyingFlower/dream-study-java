package dream.study.authorization.server.context;

import java.util.function.Supplier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;

/**
 * 自定义Security上下文
 * 
 * {@link org.springframework.security.web.context.SupplierDeferredSecurityContext}
 *
 * @author 飞花梦影
 * @date 2024-09-18 22:38:30
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public final class SupplierDeferredSecurityContext implements DeferredSecurityContext {

	private static final Log logger = LogFactory.getLog(SupplierDeferredSecurityContext.class);

	private final Supplier<SecurityContext> supplier;

	private final SecurityContextHolderStrategy strategy;

	private SecurityContext securityContext;

	private boolean missingContext;

	public SupplierDeferredSecurityContext(Supplier<SecurityContext> supplier, SecurityContextHolderStrategy strategy) {
		this.supplier = supplier;
		this.strategy = strategy;
	}

	@Override
	public SecurityContext get() {
		init();
		return this.securityContext;
	}

	@Override
	public boolean isGenerated() {
		init();
		return this.missingContext;
	}

	private void init() {
		if (this.securityContext != null) {
			return;
		}

		this.securityContext = this.supplier.get();
		this.missingContext = (this.securityContext == null);
		if (this.missingContext) {
			this.securityContext = this.strategy.createEmptyContext();
			if (logger.isTraceEnabled()) {
				logger.trace(LogMessage.format("Created %s", this.securityContext));
			}
		}
	}
}