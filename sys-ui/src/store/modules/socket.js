import SocketIO from "socket.io-client";
import {Notification} from 'element-ui'
import cache from "@/plugins/cache";

const sockets = {

  state: {
    socket: null,
  },

  mutations: {
    SET_SOCKET: (state, socket) => {
      state.socket = socket;
    }
  },

  actions: {
    OpenSocket({ commit }, xx) {
      let socket;
      if (process.env.NODE_ENV === 'production') {
        console.log('socket connect: ws://' + location.host);
        socket = SocketIO('ws://' + location.host, {
          autoConnect: true,
          query: {
            userId: cache.local.getUserId(),
            Authorization: cache.local.getAccessToken()
          },
        });
      }else{
        console.log('socket connect: ws://localhost:19011');
        socket = SocketIO('ws://localhost:19011', {
          autoConnect: true,
          query: {
            userId: cache.local.getUserId(),
            Authorization: cache.local.getAccessToken()
          },
        });
      }

      commit('SET_SOCKET', socket);

      let timeNow = new Date();
      let hours = timeNow.getHours();
      let text = '';
      if (hours >= 0 && hours <= 10) {
        text = '早上好[';
      } else if (hours > 10 && hours <= 14) {
        text = '中午好[';
      } else if (hours > 14 && hours <= 18) {
        text = '下午好[';
      } else if (hours > 18 && hours <= 24) {
        text = '晚上好[';
      }
      text = text + cache.local.getUserName() + "]";
      // 登录消息
      socket.on('notice_unread', (data) => {
        if(data > 0){
          text = text + ',  您有' + data + '条未读消息!!'
        }
        Notification.warning(text)
      });

      // 待办消息
      socket.on('notice_todo', (data) => {
        Notification.warning(data)
      });
      // 系统消息
      socket.on('notice_new', (data) => {
        Notification.warning('系统消息：' + data)
      });
      // 连接事件
      socket.on('connect_error', (error) => {
        console.error('socket connect error:', error);
      });
      socket.on('error', (error) => {
        console.error('socket error:', error);
      });
      socket.on('connecting', (error) => {
        console.log('socket connecting');
      });
      socket.on('connect', (error) => {
        console.log('socket connect success');
      });
      socket.on('connect_failed', (error) => {
        console.log('socket connect failed', error);
      });
      socket.on('disconnect', (error) => {
        console.log('socket disconnect');
      });
      socket.open();
    },
    RefreshSocket({ state, commit }, xx) {
      if (state.socket) {
        state.socket.close();
      }

      let socket;
      if (process.env.NODE_ENV === 'production') {
        console.log('socket connect: ws://' + location.host);
        socket = SocketIO('ws://' + location.host, {
          autoConnect: true,
          query: {
            userId: cache.local.getUserId(),
            Authorization: cache.local.getAccessToken()
          },
        });
      }else{
        console.log('socket connect: ws://localhost:19011');
        socket = SocketIO('ws://localhost:19011', {
          autoConnect: true,
          query: {
            userId: cache.local.getUserId(),
            Authorization: cache.local.getAccessToken()
          },
        });
      }

      commit('SET_SOCKET', socket);
      // 待办消息
      socket.on('notice_todo', (data) => {
        Notification.warning(data)
      });
      // 系统消息
      socket.on('notice_new', (data) => {
        Notification.warning('系统消息：' + data)
      });
      // 连接事件
      socket.on('connect_error', (error) => {
        console.error('socket connect error:', error);
      });
      socket.on('error', (error) => {
        console.error('socket error:', error);
      });
      socket.on('connecting', (error) => {
        console.log('socket connecting');
      });
      socket.on('connect', (error) => {
        console.log('socket connect success');
      });
      socket.on('connect_failed', (error) => {
        console.log('socket connect failed', error);
      });
      socket.on('disconnect', (error) => {
        console.log('socket disconnect');
      });
      socket.open();
    },
    CloseSocket({ state }) {
      if (state.socket) {
        state.socket.close();
      }
    },
  }
}

export default sockets
