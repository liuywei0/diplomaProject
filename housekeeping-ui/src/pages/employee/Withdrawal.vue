<template>
    <div style="margin-left:100px">
        <el-descriptions class="margin-top" title="个人信息" :column="1" v-model="employee" style="font-size:17px">
            <template slot="extra">
                <el-input placeholder="请输入提现金额" v-model="money" clearable v-if="show == true"
                    style="width:200px;margin-right:15px" size="small"></el-input>
                <el-input placeholder="请输入提现理由" v-model="dec" clearable v-if="show == true"
                    style="width:200px;margin-right:15px" size="small"></el-input>
                <el-button v-if="show == true" size="small" @click="withdrawal()">提现</el-button>
                <el-button @click="show = !show">{{ show ? "取消" : "提现" }}</el-button>

            </template>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-user"></i>
                    用户名
                </template>
                {{ employee.name }}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-mobile-phone"></i>
                    手机号
                </template>
                {{ employee.telephone }}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-male" v-if="employee.gender == '男'"></i>
                    <i class="el-icon-female" v-if="employee.gender == '女'"></i>
                    性别
                </template>
                {{ employee.gender }}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-question"></i>
                    帐号状态
                </template>
                <el-tag v-if="employee.status == '正常'"><i class="el-icon-success"></i>{{ employee.status }}</el-tag>
                <el-tag v-if="employee.status == '禁用'" type="info"><i class="el-icon-error"></i>{{ employee.status }}
                </el-tag>
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-postcard"></i>
                    身份证号
                </template>
                {{ employee.idCard }}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-set-up"></i>
                    银行卡号
                </template>
                {{ employee.bankCard }}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-odometer"></i>
                    余额
                </template>
                {{ employee.balance }}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-office-building"></i>
                    积分
                </template>
                {{ employee.score }}
            </el-descriptions-item>
        </el-descriptions>

    </div>
</template>

<script>
import axios from 'axios';
import user from '@/store/modules/user'

export default {
    data() {
        return {
            show: false,
            money: '',
            dec: '',
            employee: [],
        }
    },
    created() {
        this.loadData();
    },
    methods: {

        // 提现
        withdrawal() {
            axios({
                method: 'get',
                url: 'http://localhost:8080/account/withdrawal',
                params: { userId: this.employee.id, money: this.money, dec: this.dec }
            }).then((response) => {
                if (response.data.code === 200) {
                    // 成功弹窗
                    this.$message({
                        message: response.data.message,
                        type: 'success'
                    });
                    this.show = false;
                } else {
                    alert(response.data.message);
                }
            });
        },

        loadData() {
            axios({
                method: 'get',
                url: 'http://localhost:8080/user/findByTel',
                params: { tel: user.state.token }
            }).then((response) => {
                if (response.data.code === 200) {
                    this.employee = response.data.data;
                } else {
                    alert(response.data.message);
                }
            });
        },
    }
}
</script>

<style>
.el-descriptions :not(.is-bordered) .el-descriptions-item__cell {
    padding-bottom: 20px;
}
</style>