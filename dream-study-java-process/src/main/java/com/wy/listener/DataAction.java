package com.wy.listener;
// package com.huoli.carapp.action;
//
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;
//
// import org.apache.log4j.Logger;
// import org.springframework.beans.factory.annotation.Autowired;
// import
// org.springframework.context.support.ReloadableResourceBundleMessageSource;
// import org.springframework.web.bind.annotation.ExceptionHandler;
//
/// **
// *
// * @author ZengLei
// */
//
// public class DataAction extends AbsCarAction {
// private static final Logger logger = Logger.getLogger(DataAction.class);
// private static ExecutorService EXE = Executors.newFixedThreadPool(10);
//
// @Autowired
// private ReloadableResourceBundleMessageSource reloadableMessageSource;
//
// protected <E> void print(CmnMsg resp, int logLevel) {
// String url = ServletUtil
// .httpUrl(AppCtx.req(), logLevel > 0, null, null);
// String ip = ServletUtil.ip(AppCtx.req());
// String auth = this.header(Auth.KEY_AUTH);
// long time = AppCtx.time();
// time = System.currentTimeMillis() - time;
// int code = resp!=null?resp.code():0;
// String msg =resp!=null?resp.msg():null;
// if (logLevel == 2) {
// logger.info("action#time:" + time + "#ip:"
// + ip + "#url:" + url + "#auth:" + auth + "#code:" + code
// + "#msg:" + msg + "#resp:" + JsonUtil.toJson(resp));
// } else {
// logger.info("action#time:" + time + "#ip:"
// + ip + "#url:" + url + "#auth:" + auth + "#code:" + code
// + "#msg:" + msg);
// }
// if ("xml".equalsIgnoreCase(this.param("format"))) {
// this.printXml("res", resp);
// } else {
// this.printJson(resp);
// }
// }
//
// protected <E> void print(CmnMsg resp) {
// this.print(resp, 2);
// }
//
// @ExceptionHandler
// protected void error(final Exception error) {
// EXE.submit(new Runnable() {
// public void run() {
// ApiWxAlarm.alarms(error.getMessage(), ProperPro.getValueCn(null,
// reloadableMessageSource, "weixin_openid"));
// }
// });
// logger.error(null, error);
// this.print(Resp.respErr(Resp.ERROR_NUKNOWN));
// }
// }
