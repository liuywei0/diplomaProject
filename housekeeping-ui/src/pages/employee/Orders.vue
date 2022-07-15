<template>
    <div class="order">
        <el-tabs type="card" v-model="param.status">
            <el-tab-pane label="未付款订单" name="未付款"></el-tab-pane>
            <el-tab-pane label="待服务订单" name="未服务"></el-tab-pane>
            <el-tab-pane label="待评价订单" name="待评价"></el-tab-pane>
            <el-tab-pane label="已完成订单" name="已完成"></el-tab-pane>
        </el-tabs>

        <!-- 表单 -->
        <el-table :data="orders" style="width: 100%;margin-bottom: 20px;" row-key="id">
            <el-table-column type="expand">
                <template v-slot="scope">
                    <el-table :data="scope.row.orderLines" size="mini">
                        <el-table-column label="名称" prop="productName"></el-table-column>
                        <el-table-column label="单价" prop="productPrice"></el-table-column>
                        <el-table-column label="数量" prop="number"></el-table-column>
                        <el-table-column label="描述" prop="productIntroduce"></el-table-column>
                    </el-table>
                </template>
            </el-table-column>
            <el-table-column type="index" :index="1" label="编号" align="center" width="60">
            </el-table-column>
            <el-table-column prop="orderTime" label="订单时间" sortable>
            </el-table-column>
            <el-table-column prop="total" label="总价（元）" sortable width="120">
            </el-table-column>
            <el-table-column prop="status" label="状态" sortable width="120">
            </el-table-column>
            <el-table-column prop="customer.name" label="顾客" sortable width="120">
            </el-table-column>
            <el-table-column prop="employee.name" label="员工" sortable width="120">
            </el-table-column>
            <el-table-column label="地址" sortable width="250">
                <template v-slot="scope">
                    <div>{{ scope.row.address.username + " " + scope.row.address.telephone }}</div>
                    <div>{{ scope.row.address.province + " " + scope.row.address.city + " " + scope.row.address.area }}
                    </div>
                    <div>{{ scope.row.address.address }}</div>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="200">
                <template v-slot="scope">
                    <el-tag type="info" v-if="scope.row.status == '未付款'">未付款</el-tag>

                    <el-button type="primary" v-if="scope.row.status == '未服务'" size="small"
                        @click="serviceCus(scope.row.id)">完成服务</el-button>
                    <el-button type="danger" v-if="scope.row.status == '未服务'" size="small"
                        @click="refOrder(scope.row.id)">拒绝服务</el-button>

                    <el-tag type="info" v-if="scope.row.status == '待评价'">待评价</el-tag>

                    <el-tag type="success" v-if="scope.row.status == '已完成'">已完成</el-tag>
                </template>
            </el-table-column>
        </el-table>

    </div>
</template>

<script>
import axios from 'axios';
import { getDate } from '@/utils/common'
import user from '@/store/modules/user'

export default {
    data() {
        return {
            param: {
                status: '未付款',
                employeeId: ''
            },
            employee: '',
            orders: [],
            form: {},
            id: '',
            show: true,
        }
    },

    created() {
        this.loadUser();
        this.loadData();

    },

    watch: {
        // 监听param的变化
        param: {
            handler: function () {
                this.loadData();
            },
            deep: true
        }
    },
    methods: {

        // 员工拒绝服务
        refOrder(id) {
            axios({
                method: 'get',
                url: 'http://localhost:8080/order/refOrder',
                params: { id: id }
            }).then((response) => {
                if (response.data.code === 200) {
                    // 成功弹窗
                    this.$message({
                        message: response.data.message,
                        type: 'success'
                    });
                    // 刷新
                    this.loadData();
                } else {
                    alert(response.data.message);
                }
            });
        },

        // 已完成服务
        serviceCus(id) {
            axios({
                method: 'get',
                url: 'http://localhost:8080/order/serviceCus',
                params: { id: id }
            }).then((response) => {
                if (response.data.code === 200) {
                    // 成功弹窗
                    this.$message({
                        message: response.data.message,
                        type: 'success'
                    });
                    // 刷新
                    this.loadData();
                } else {
                    alert(response.data.message);
                }
            });
        },

        loadData() {
            // 复制param
            let param = Object.assign({}, this.param);

            axios({
                method: 'get',
                url: 'http://localhost:8080/order/findByEmp',
                params: param
            }).then((response) => {
                if (response.data.code === 200) {

                    response.data.data.forEach(order => {
                        order.orderTime = getDate(order.orderTime, 'yyyy年MM月dd日 hh:mm:ss');
                    });

                    this.orders = response.data.data;

                } else {
                    alert(response.data.message);
                }
            });
        },

        loadUser() {
            axios({
                method: 'get',
                url: 'http://localhost:8080/user/findByTel',
                params: { tel: user.state.token }
            }).then((response) => {
                if (response.data.code === 200) {
                    this.employee = response.data.data;
                    this.param.employeeId = this.employee.id;
                } else {
                    alert(response.data.message);
                }
            });
        },

    }
}

</script>

<style>
</style>