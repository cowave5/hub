<template>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" v-if="!topNav"/>
    <top-nav id="topmenu-container" class="topmenu-container" v-if="topNav"/>

    <div class="right-menu">
      <template v-if="device!=='mobile'">
        <search id="header-search" class="right-menu-item"/>
        <notification id="notification" @click.native="showNotification()" class="right-menu-item hover-effect"/>
        <messages id="messages"  class="right-menu-item hover-effect"/>
        <language-select id="language-select" class="right-menu-item hover-effect"/>
        <ruo-yi-git id="ruo-yi-git" class="right-menu-item hover-effect"/>
        <size-select id="size-select" class="right-menu-item hover-effect"/>
        <screenfull id="screenfull" class="right-menu-item hover-effect"/>
      </template>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img :src="avatar" class="user-avatar" :alt="name" :title="name"><i class="el-icon-caret-boRttom"/>
        </div>
        <el-dropdown-menu slot="dropdown">
          <template v-if="topNav">
            <router-link to="/user/profile">
              <el-dropdown-item>{{ name }}</el-dropdown-item>
            </router-link>
          </template>
          <template v-else>
            <router-link to="/user/profile">
              <el-dropdown-item>{{ name }}</el-dropdown-item>
            </router-link>
            <router-link to="/user/notice">
              <el-dropdown-item>系统消息</el-dropdown-item>
            </router-link>
          </template>
          <el-dropdown-item divided  @click.native="setting = true">
            <span>{{$t('commons.theme.preference')}}</span>
          </el-dropdown-item>
          <el-dropdown-item @click.native="logout">
            <span>{{$t('commons.button.logout')}}</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

    <el-dialog title=">>系统消息" :visible.sync="notifyOpen" width="60%" append-to-body>
      <el-table :data="msgList" :show-header="false" @row-click="handleRowClick" style="margin-top: 15px;">
          <el-table-column prop="publishTime" align="center" width="160">
            <template slot-scope="scope">
              <span v-if="scope.row.readStatus === 0" class="red-point">{{ scope.row.publishTime }}</span>
              <span v-else>{{ scope.row.publishTime }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" width="100">
            <template slot-scope="scope">
              <template v-for="item in dict.type.notice_level">
                <span v-if="scope.row.noticeLevel === item.value">{{ $t(item.name) }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column prop="createBy" align="center" width="120"/>
          <el-table-column align="center" width="100">
            <template slot-scope="scope">
              <template v-for="item in dict.type.notice_type">
                <span v-if="scope.row.noticeType === item.value">{{ $t(item.name) }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column prop="noticeTitle" align="left" :show-overflow-tooltip="true"/>
        </el-table>
        <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>
    </el-dialog>

    <notice-info ref="noticeInfo"/>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import TopNav from '@/components/TopNav'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import LanguageSelect from '@/components/LanguageSelect'
import Notification from '@/components/notification'
import Messages from '@/components/messages'
import Search from '@/components/HeaderSearch'
import RuoYiGit from '@/components/RuoYi/Git'
import RuoYiDoc from '@/components/RuoYi/Doc'
import { getNoticeMsg, readNoticeMsg } from '@/api/system/notice'

export default {
  components: {
    Breadcrumb,
    TopNav,
    Hamburger,
    Screenfull,
    SizeSelect,
    LanguageSelect,
    Notification,
    Messages,
    Search,
    RuoYiGit,
    RuoYiDoc,
    noticeInfo: ()=> import('./msg.vue')
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'device',
      'name'
    ]),
    setting: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    },
    topNav: {
      get() {
        return this.$store.state.settings.topNav
      }
    }
  },
  dicts: ['notice_level', 'notice_type'],
  data() {
    return {
      notifyOpen: false,
      msgList: [],
      total: 0,
      queryParams: {
        page: 1,
        pageSize: 10
      },
    }
  },
  methods: {
    showNotification() {
      this.notifyOpen = true;
      this.getList();
    },
    getList() {
      getNoticeMsg(this.queryParams).then(response => {
        this.msgList = response.data.list;
        this.total = response.data.total;
      });
    },
    handleRowClick(row, column, event){
      if(row.readStatus === 0){
        readNoticeMsg(row.noticeId).then(() => {
          row.readStatus = 10;
          this.$refs.noticeInfo.show(row);
          this.$store.dispatch('refreshNoticeCount');
        });
      }else{
        this.$refs.noticeInfo.show(row);
      }
    },
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      this.$confirm(this.$t('commons.confirm.logout'),  {
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/index';
        })
      }).catch(() => {});
    },
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .right-menu-item2 {
      display: inline-block;
      padding: 0 0px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;
        font-size: small;

        .user-avatar {
          cursor: pointer;
          width: 36px;
          height: 36px;
          border-radius: 50%;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}

.el-table {
  cursor: pointer;
}

.red-point{
  position: relative;
}

.red-point::before{
  content: " ";
  border: 4px solid #1890ff;
  border-radius:3px;
  position: absolute;
  z-index: 1000;
  left: 0;
  top: 1px;
  margin-left: -10px;
}
</style>
