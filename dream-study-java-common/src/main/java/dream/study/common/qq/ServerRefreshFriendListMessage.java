package dream.study.common.qq;

/**
 * 服务器刷新好友列表
 */
public class ServerRefreshFriendListMessage extends Message {

	@Override
	public int getMessageType() {
		return SERVER_TO_CLIENT_REFRESHFRIENDLIST;
	}
}