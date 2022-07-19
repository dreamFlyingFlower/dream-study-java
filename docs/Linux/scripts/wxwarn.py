# 此脚本通过企业微信应用,进行微信告警,可用于Zabbix监控
# -*- coding: utf-8 -*-

import requests
import json


class DLF:
    def __init__(self, corpid, corpsecret):
        self.url = "https://qyapi.weixin.qq.com/cgi-bin"
        self.corpid = corpid
        self.corpsecret = corpsecret
        self._token = self._get_token()

    def _get_token(self):
        '''
        获取企业微信API接口的access_token
        :return:
        '''
        token_url = self.url + "/gettoken?corpid=%s&corpsecret=%s" %(self.corpid, self.corpsecret)
        try:
            res = requests.get(token_url).json()
            token = res['access_token']
            return token
        except Exception as e:
            return str(e)

    def _get_media_id(self, file_obj):
        get_media_url = self.url + "/media/upload?access_token={}&type=file".format(self._token)
        data = {"media": file_obj}

        try:
            res = requests.post(url=get_media_url, files=data)
            media_id = res.json()['media_id']
            return media_id
        except Exception as e:
            return str(e)

    def send_text(self, agentid, content, touser=None, toparty=None):
        send_msg_url = self.url + "/message/send?access_token=%s" % (self._token)
        send_data = {
            "touser": touser,
            "toparty": toparty,
            "msgtype": "text",
            "agentid": agentid,
            "text": {
                "content": content
            }
        }

        try:
            res = requests.post(send_msg_url, data=json.dumps(send_data))
        except Exception as e:
            return str(e)

    def send_image(self, agentid, file_obj, touser=None, toparty=None):
        media_id = self._get_media_id(file_obj)
        send_msg_url = self.url + "/message/send?access_token=%s" % (self._token)
        send_data = {
            "touser": touser,
            "toparty": toparty,
            "msgtype": "image",
            "agentid": agentid,
            "image": {
                "media_id": media_id
           }
        }

        try:
            res = requests.post(send_msg_url, data=json.dumps(send_data))
        except Exception as e:
            return str(e)