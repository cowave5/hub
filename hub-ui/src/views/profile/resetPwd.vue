<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="150px">
    <el-form-item :label="$t('user.label.pwd_old')" prop="oldPassword">
      <el-input v-model="user.oldPassword" :placeholder="$t('user.placeholder.pwd_old')" type="password" show-password/>
    </el-form-item>
    <el-form-item :label="$t('user.label.pwd_new')" prop="newPassword">
      <el-input v-model="user.newPassword" :placeholder="$t('user.placeholder.pwd_new')" type="password" show-password/>
    </el-form-item>
    <el-form-item :label="$t('user.label.pwd_confirm')" prop="confirmPassword">
      <el-input v-model="user.confirmPassword" :placeholder="$t('user.placeholder.pwd_confirm')" type="password" show-password/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">{{$t('commons.button.confirm')}}</el-button>
      <el-button type="danger" size="mini" @click="close">{{$t('commons.button.cancel')}}</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { resetPasswd } from "@/api/profile";

export default {
  data() {
    return {
      user: {
        oldPassword: undefined,
        newPassword: undefined,
        confirmPassword: undefined
      },
    };
  },
  watch:{
    "$i18n.locale": function () {
      this.$nextTick(() => {
        this.$refs.form.clearValidate()
      })
    }
  },
  computed: {
    rules() {
      return {
        oldPassword: [
          { required: true, message: this.$t('user.rules.pwd_old'), trigger: "blur" }
        ],
        newPassword: [
          { required: true, message: this.$t('user.rules.pwd_new'), trigger: "blur" },
          { min: 6, max: 20, message: this.$t('user.rules.passwd_len'), trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, message: this.$t('user.rules.pwd_confirm'), trigger: "blur" },
          { required: true, validator: this.equalToPassword, trigger: "blur" }
        ]
      }
    }
  },
  methods: {
    equalToPassword(rule, value, callback) {
      if (this.user.newPassword !== value) {
        callback(new Error(this.$t('user.rules.pwd_compare')));
      } else {
        callback();
      }
    },
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          resetPasswd(this.user.oldPassword, this.user.newPassword).then(response => {
            this.$modal.msgSuccess(this.$t('commons.msg.success.reset'));
            this.user = {
                oldPassword: undefined,
                newPassword: undefined,
                confirmPassword: undefined
            }
          });
        }
      });
    },
    close() {
      this.$tab.closePage();
    }
  }
};
</script>
