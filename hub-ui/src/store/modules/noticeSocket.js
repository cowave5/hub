import SocketIO from "socket.io-client";
import {Notification} from 'element-ui'
import cache from "@/plugins/cache";
import { countUnReadMsg } from '@/api/system/notice'

const sockets = {

  state: {
    noticeSocket: null,
    noticeCount: 0
  },

  mutations: {
    SET_SOCKET: (state, socket) => {
      state.noticeSocket = socket;
    },
    SET_NOTICE_COUNT: (state, noticeCount) => {
      state.noticeCount = noticeCount
    }
  },

  actions: {
    OpenNoticeSocket({ state, commit }, xx) {
      if(!cache.local.getUserId() || !cache.local.getAccessToken()){
        return;
      }
      if (state.noticeSocket) {
        return;
      }

      let socket;
      if (process.env.NODE_ENV === 'production') {
        socket = SocketIO('ws://' + location.host + '/notice', {
          autoConnect: true,
          path: '/socket.io',
          transports: ['websocket'],
          query: {
            clientId: cache.local.getUserCode(),
            Authorization: cache.local.getAccessToken()
          },
        });
      }else{
        socket = SocketIO('ws://localhost:19011/notice', {
          autoConnect: true,
          path: '/socket.io',
          transports: ['websocket'],
          query: {
            clientId: cache.local.getUserCode(),
            Authorization: cache.local.getAccessToken()
          },
        });
      }
      commit('SET_SOCKET', socket);

      // 消息统计
      socket.on('noticeCount', (noticeCount) => {
        commit('SET_NOTICE_COUNT', noticeCount);
      })
      // 系统消息
      socket.on('newNotice', (data) => {
        Notification.warning('系统消息：' + data)
        socket.emit('getNoticeCount', cache.local.getUserCode());
      });
      // 流程消息
      socket.on('flow_notice', (data) => {
        Notification.warning(data)
        socket.emit('getNoticeCount', cache.local.getUserCode());
      });

      // 连接事件
      socket.on('connect_error', (error) => {
        console.error('notice socket error:', error);
      });
      socket.on('error', (error) => {
        console.error('notice socket error:', error);
      });
      socket.on('connecting', (error) => {
        console.log('notice socket connecting');
      });
      socket.on('connect', (error) => {
        console.log('notice socket success');
        socket.emit('getNoticeCount', cache.local.getUserCode());
      });
      socket.on('connect_failed', (error) => {
        console.log('notice socket failed', error);
      });
      socket.on('disconnect', (error) => {
        console.log('notice socket disconnect');
      });
      socket.open();
    },

    RefreshNoticeSocket({ state, commit }, xx) {
      if (state.noticeSocket) {
        state.noticeSocket.close();
      }

      let socket;
      if (process.env.NODE_ENV === 'production') {
        socket = SocketIO('ws://' + location.host + '/notice', {
          autoConnect: true,
          path: '/socket.io',
          transports: ['websocket'],
          query: {
            clientId: cache.local.getUserCode(),
            Authorization: cache.local.getAccessToken()
          },
        });
      }else{
        socket = SocketIO('ws://localhost:19011/notice', {
          autoConnect: true,
          path: '/socket.io',
          transports: ['websocket'],
          query: {
            clientId: cache.local.getUserCode(),
            Authorization: cache.local.getAccessToken()
          },
        });
      }
      commit('SET_SOCKET', socket);

      // 消息统计
      socket.on('noticeCount', (noticeCount) => {
        commit('SET_NOTICE_COUNT', noticeCount);
      })
      // 系统消息
      socket.on('newNotice', (data) => {
        Notification.warning('系统消息：' + data)
        socket.emit('getNoticeCount', cache.local.getUserCode());
      })
      // 流程消息
      socket.on('flow_notice', (data) => {
        Notification.warning(data)
        socket.emit('getNoticeCount', cache.local.getUserCode());
      });

      // 连接事件
      socket.on('connect_error', (error) => {
        console.error('notice socket error:', error);
      });
      socket.on('error', (error) => {
        console.error('notice socket error:', error);
      });
      socket.on('connecting', (error) => {
        console.log('notice socket connecting');
      });
      socket.on('connect', (error) => {
        console.log('notice socket success');
        socket.emit('getNoticeCount', cache.local.getUserCode());
      });
      socket.on('connect_failed', (error) => {
        console.log('notice socket failed', error);
      });
      socket.on('disconnect', (error) => {
        console.log('notice socket disconnect');
      });
      socket.open();
    },

    CloseNoticeSocket({ state }) {
      if (state.noticeSocket) {
        state.noticeSocket.close();
      }
    },

    refreshNoticeCount({ commit, state }) {
      return new Promise((resolve, reject) => {
        countUnReadMsg().then(res => {
          commit('SET_NOTICE_COUNT', res.data)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}

export default sockets
