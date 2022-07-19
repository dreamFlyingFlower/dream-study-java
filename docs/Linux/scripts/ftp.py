# 通过 ftplib 模块操作 ftp 服务器,进行上传下载等操作
# -*- coding: utf-8 -*-

from ftplib import FTP
from os import path
import copy


class FTPClient:
    def __init__(self, host, user, passwd, port=21):
        self.host = host
        self.user = user
        self.passwd = passwd
        self.port = port
        self.res = {'status': True, 'msg': None}
        self._ftp = None
        self._login()

    def _login(self):
        '''
        登录FTP服务器
        :return: 连接或登录出现异常时返回错误信息
        '''
        try:
            self._ftp = FTP()
            self._ftp.connect(self.host, self.port, timeout=30)
            self._ftp.login(self.user, self.passwd)
        except Exception as e:
            return e

    def upload(self, localpath, remotepath=None):
        '''
        上传ftp文件
        :param localpath: local file path
        :param remotepath: remote file path
        :return:
        '''
        if not localpath: return 'Please select a local file. '
        # 读取本地文件
        # fp = open(localpath, 'rb')

        # 如果未传递远程文件路径，则上传到当前目录，文件名称同本地文件
        if not remotepath:
            remotepath = path.basename(localpath)

        # 上传文件
        self._ftp.storbinary('STOR ' + remotepath, localpath)
        # fp.close()

    def download(self, remotepath, localpath=None):
        '''
        localpath
        :param localpath: local file path
        :param remotepath: remote file path
        :return:
        '''

        if not remotepath: return 'Please select a remote file. '
        # 如果未传递本地文件路径，则下载到当前目录，文件名称同远程文件
        if not localpath:
            localpath = path.basename(remotepath)
        # 如果localpath是目录的话就和remotepath的basename拼接
        if path.isdir(localpath):
            localpath = path.join(localpath, path.basename(remotepath))

        # 写入本地文件
        fp = open(localpath, 'wb')

        # 下载文件
        self._ftp.retrbinary('RETR ' + remotepath, fp.write)
        fp.close()

    def nlst(self, dir='/'):
        '''
        查看目录下的内容
        :return: 以列表形式返回目录下的所有内容
        '''
        files_list = self._ftp.nlst(dir)
        return files_list

    def rmd(self, dir=None):
        '''
        删除目录
        :param dir: 目录名称
        :return: 执行结果
        '''
        if not dir: return 'Please input dirname'
        res = copy.deepcopy(self.res)
        try:
            del_d = self._ftp.rmd(dir)
            res['msg'] = del_d
        except Exception as e:
            res['status'] = False
            res['msg'] = str(e)

        return res

    def mkd(self, dir=None):
        '''
        创建目录
        :param dir: 目录名称
        :return: 执行结果
        '''
        if not dir: return 'Please input dirname'
        res = copy.deepcopy(self.res)
        try:
            mkd_d = self._ftp.mkd(dir)
            res['msg'] = mkd_d
        except Exception as e:
            res['status'] = False
            res['msg'] = str(e)

        return res

    def del_file(self, filename=None):
        '''
        删除文件
        :param filename: 文件名称
        :return: 执行结果
        '''
        if not filename: return 'Please input filename'
        res = copy.deepcopy(self.res)
        try:
            del_f = self._ftp.delete(filename)
            res['msg'] = del_f
        except Exception as e:
            res['status'] = False
            res['msg'] = str(e)

        return res

    def get_file_size(self, filenames=[]):
        '''
        获取文件大小,单位是字节
        判断文件类型
        :param filename: 文件名称
        :return: 执行结果
        '''
        if not filenames: return {'msg': 'This is an empty directory'}
        res_l = []
        for file in filenames:
            res_d = {}
            # 如果是目录或者文件不存在就会报错
            try:
                size = self._ftp.size(file)
                type = 'f'
            except:
                # 如果是路径的话size显示 - , file末尾加/ （/dir/）
                size = '-'
                type = 'd'
                file = file + '/'

            res_d['filename'] = file
            res_d['size'] = size
            res_d['type'] = type
            res_l.append(res_d)

        return res_l

    def rename(self, old_name=None, new_name=None):
        '''
        重命名
        :param old_name: 旧的文件或者目录名称
        :param new_name: 新的文件或者目录名称
        :return: 执行结果
        '''
        if not old_name or not new_name: return 'Please input old_name and new_name'
        res = copy.deepcopy(self.res)
        try:
            rename_f = self._ftp.rename(old_name, new_name)
            res['msg'] = rename_f
        except Exception as e:
            res['status'] = False
            res['msg'] = str(e)

        return res

    def close(self):
        '''
        退出ftp连接
        :return:
        '''
        try:
            # 向服务器发送quit命令
            self._ftp.quit()
        except Exception:
            return 'No response from server'
        finally:
            # 客户端单方面关闭连接
            self._ftp.close()