<template>
  <el-form ref="form" :model="user" :rules="rules" :disabled="user.userType !== 'sys'" label-width="80px">
    <el-form-item :label="$t('user.label.name')" prop="userName">
      <el-input v-model="user.userName" maxlength="30" />
    </el-form-item>
    <el-form-item :label="$t('user.label.phone')" prop="userPhone">
      <el-input v-model="user.userPhone" maxlength="11" />
    </el-form-item>
    <el-form-item :label="$t('user.label.email')" prop="userEmail">
      <el-input v-model="user.userEmail" maxlength="50" />
    </el-form-item>
    <el-form-item :label="$t('user.label.sex')">
      <el-radio-group v-model="user.userSex">
        <el-radio v-for="dict in dict.type.sex" :key="dict.value" :label="dict.value">{{$t(dict.name)}}</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">{{$t('commons.button.save')}}</el-button>
      <el-button type="danger" size="mini" @click="close">{{$t('commons.button.close')}}</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateUserProfile } from "@/api/profile";

export default {
  props: {
    user: {
      type: Object
    }
  },
  dicts: ['sex'],
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
        userName: [
          { required: true, message: this.$t('user.rules.name'), trigger: "blur" }
        ],
        userEmail: [
          { type: "email", message: this.$t('user.rules.email'), trigger: ["blur", "change"] }
        ],
        userPhone: [
          { pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: this.$t('user.rules.phone'), trigger: "blur" }
        ]
      }
    }
  },
  methods: {
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateUserProfile(this.user).then(response => {
            this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
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
