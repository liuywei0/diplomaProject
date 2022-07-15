<template>
    <div class="order">
        <el-tabs type="card" v-model="param.status">
            <el-tab-pane label="我的购物车" name="未付款"></el-tab-pane>
            <el-tab-pane label="待派单订单" name="待派单"></el-tab-pane>
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
                    <el-button type="primary" @click="pay(scope.row.id)" v-if="scope.row.status == '未付款'" size="small">
                        支付
                    </el-button>

                    <el-tag type="info" v-if="scope.row.status == '待派单'">待派单</el-tag>
                    <el-button type="primary" v-if="scope.row.status == '待派单'" size="small" style="margin-left:10px"
                        @click="openRef(scope.row.id)">
                        申请退款</el-button>

                    <el-tag type="info" v-if="scope.row.status == '未服务'">待服务</el-tag>
                    <el-button type="primary" v-if="scope.row.status == '未服务'" size="small" style="margin-left:10px"
                        @click="openRef(scope.row.id)">
                        申请退款</el-button>

                    <el-button type="primary" @click="toAdd(scope.row)" v-if="scope.row.status == '待评价'" size="small">
                        评价
                    </el-button>
                    <el-button type="primary" v-if="scope.row.status == '待评价'" size="small" style="margin-left:10px"
                        @click="openRef(scope.row.id)">
                        申请退款</el-button>

                    <el-tag type="success" v-if="scope.row.status == '已完成'">已完成</el-tag>
                    <el-button type="primary" @click="toAdd(scope.row)" v-if="scope.row.status == '已完成'" size="small"
                        style="margin-left:10px">追评
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog title="评价" :visible.sync="visible" width="37%">
            <el-form :model="comment" label-width="100px">
                <p style="font-size:20px; margin-left:50px">请对此次服务做出评价：</p>
                <el-form-item>
                    <el-rate v-model="comment.score" show-text>
                    </el-rate>
                    <el-input v-model="comment.content" style="width:400px;margin-top: 10px;"></el-input>
                </el-form-item>
                <el-form-item align="right">
                    <el-button @click="toClose()">取 消</el-button>
                    <el-button type="primary" @click="comments()">确 定</el-button>
                </el-form-item>
            </el-form>

        </el-dialog>

        <el-dialog title="退款申请" :visible.sync="refvbe" width="25%">
            <el-form :model="refunds" label-width="125px">
                <el-form-item label="请输入退款金额：">
                    <el-input v-model="refunds.money" style="width:200px;margin-top: 10px;"></el-input>
                </el-form-item>

                <el-form-item label="请输入退款理由：">
                    <el-input v-model="refunds.msg" style="width:200px;margin-top: 10px;"></el-input>
                </el-form-item>
                <el-form-item align="right">
                    <el-button @click="toClose()">取 消</el-button>
                    <el-button type="primary" @click="refund()">确 定</el-button>
                </el-form-item>
            </el-form>

        </el-dialog>
    </div>
</template>

<script>
import axios from 'axios';
import { getDate } from '@/utils/common'
import user from '@/store/modules/user'

export default {
    data() {
        return {
            dialogVisible: false,
            visible: false,
            refvbe: false,
            param: {
                status: '未付款',
                customerId: ''
            },
            orders: [],
            form: {},
            employee: [],
            refunds: {},
            id: '',
            comment: {
                id: '',
                userId: ''
            },
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
        // 打开评价对话框
        toAdd(row) {
            this.comment.id = row.id;
            this.comment.userId = row.customerId;
            console.log(this.comment)
            this.visible = true;
        },

        // 关闭对话框
        toClose() {
            this.visible = false;
            this.comment = {};
            this.refvbe = false;
            this.refunds ={};
        },

        openRef(id) {
            this.refunds.id = id;
            this.refvbe = true;
        },
        // 退款
        refund(id) {

            axios({
                method: 'get',
                url: 'http://localhost:8080/order/refund',
                params: this.refunds
            }).then((response) => {
                if (response.data.code === 200) {
                    // 成功弹窗
                    this.$message({
                        message: response.data.message,
                        type: 'success'
                    });
                    // 关闭弹窗
                    this.refvbe = false;
                } else {
                    alert(response.data.message);
                }
            });


        },

        // 支付订单
        pay(id) {
            axios({
                method: 'get',
                url: 'http://localhost:8080/order/pay',
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

        // 评价
        comments() {
            axios({
                method: 'post',
                url: 'http://localhost:8080/order/comments',
                params: this.comment
            }).then((response) => {
                console.log(this.comment)
                if (response.data.code === 200) {
                    // 成功弹窗
                    this.$message({
                        message: response.data.message,
                        type: 'success'
                    });
                    // 关闭评价对话框
                    this.toClose();
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
                url: 'http://localhost:8080/order/findByCus',
                params: param
            }).then((response) => {
                if (response.data.code === 200) {

                    response.data.data.forEach(order => {
                        order.orderTime = getDate(order.orderTime, 'yyyy年MM月dd日 hh:mm:ss');
                    });

                    this.orders = response.data.data;
                    console.log(this.orders)

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

                    this.customer = response.data.data;
                    this.param.customerId = this.customer.id;
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