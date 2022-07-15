<template>
  <div class="navbar">
    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb class="breadcrumb-container" />

    <div class="right-menu">
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <img :src="avatar + '?imageView2/1/w/80/h/80'" class="user-avatar">
          <!-- <img :src="avatar" class="user-avatar"> -->

          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown" class="user-dropdown">
          <el-dropdown-item>
            {{ user.role.name }}：{{ user.name }}
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="visible = true">
            修改信息
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="diaVisible = true">
            修改密码
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="logout">
            <span style="display:block;">退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

    <el-dialog title="个人信息" :visible.sync="visible" width="37%">
      <el-form :model="user" label-width="125px">
        <el-form-item label="用户名：">
          <el-input v-model="user.name" style="width:400px;"></el-input>
        </el-form-item>

        <el-form-item label="手机号：">
          <el-input v-model="user.telephone" style="width:400px;"></el-input>
        </el-form-item>

        <el-form-item label="银行卡号码：">
          <el-input v-model="user.bankCard" style="width:400px;"></el-input>
        </el-form-item>

        <el-form-item label="身份证号码：">
          <el-input v-model="user.idCard" style="width:400px;" :readonly="true"></el-input>
        </el-form-item>

        <el-form-item label="注册时间：">
          <el-input v-model="user.registerTime" style="width:400px;" :readonly="true"></el-input>
        </el-form-item>

        <el-form-item label="积分：">
          <el-input v-model="user.score" style="width:400px;" :readonly="true"></el-input>
        </el-form-item>

        <el-form-item label="余额：">
          <el-input v-model="user.balance" style="width:400px;" :readonly="true"></el-input>
        </el-form-item>

        <el-form-item label="身份：">
          <el-input v-model="user.role.name" style="width:400px;" :readonly="true"></el-input>
        </el-form-item>

        <el-form-item align="right">
          <el-button @click="visible = false">取 消</el-button>
          <el-button type="primary" @click="edit">确 定</el-button>
        </el-form-item>
      </el-form>

    </el-dialog>

    <!-- 修改密码 -->
    <el-dialog title="修改密码" :visible.sync="diaVisible" width="30%">
      <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="原密码：" prop="psw">
          <el-input type="password" v-model="ruleForm.psw" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="新密码：" prop="pass">
          <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码：" prop="checkPass">
          <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item align="right">
          <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
          <el-button @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import axios from 'axios';
import { getDate } from '@/utils/common'

export default {
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass');
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.ruleForm.pass) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      diaVisible: false,
      visible: false,
      user: {
        name: '',
        telephone: '',
        password: '',
        bankCard: '',
        role: {
          name: ''
        }
      },
      ruleForm: {
        pass: '',
        checkPass: '',
        psw: ''
      },
      rules: {
        psw: [
          { validator: validatePass, trigger: 'blur' }
        ],
        pass: [
          { validator: validatePass, trigger: 'blur' }
        ],
        checkPass: [
          { validator: validatePass2, trigger: 'blur' }
        ],
      }

    }
  },
  created() {
    this.loadUser();
  },
  components: {
    Breadcrumb,
    Hamburger
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
    ]),
    ...mapState('user', ['token']),
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$message.success("退出登录成功")
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    },
    edit() {
      this.user.registerTime = new Date(this.user.registerTime).getTime();

      axios({
        method: 'post',
        url: 'http://localhost:8080/user/saveOrUpdate',
        params: this.user
      }).then((response) => {
        if (response.data.code === 200) {
          // 成功弹窗
          this.$message({
            message: response.data.message,
            type: 'success'
          });
          // 关闭弹窗
          this.visible = false
        } else {
          this.$message.error(response.data.message);
        }
      });

    },
    loadUser() {
      axios({
        method: 'get',
        url: 'http://localhost:8080/user/findByTel',
        params: { tel: this.token }
      }).then((response) => {
        if (response.data.code === 200) {

          this.user = response.data.data;
          this.user.registerTime = getDate(this.user.registerTime, 'yyyy-MM-dd hh:mm:ss');
        } else {
          alert(response.data.message);
        }
      });
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios({
            method: 'get',
            url: 'http://localhost:8080/user/editPsw',
            params: { id: this.user.id, psw: this.ruleForm.psw, pass: this.ruleForm.pass }
          }).then((response) => {
            if (response.data.code === 200) {
              this.$message.success(response.data.message)
            } else {
              this.$message.error(response.data.message);
            }
            this.diaVisible = false;
          });
        } else {
          this.$message.error('错误!!');
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
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

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
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
</style>
