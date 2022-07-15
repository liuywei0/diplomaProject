<template>
    <div class="order">
        <el-tabs type="card" v-model="param.status">
            <el-tab-pane label="所有订单" name="all"></el-tab-pane>
            <el-tab-pane label="未付款订单" name="未付款"></el-tab-pane>
            <el-tab-pane label="待派单订单" name="待派单"></el-tab-pane>
            <el-tab-pane label="未服务订单" name="未服务"></el-tab-pane>
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
                    <el-button type="primary" @click="open(scope.row)" v-if="scope.row.status == '待派单'" size="small">派单
                    </el-button>
                    <el-button type="primary" @click="serviceCus(scope.row.id)" v-if="scope.row.status == '未服务'"
                        size="small">服务</el-button>
                    <el-button type="danger" @click="refOrder(scope.row.id)" v-if="scope.row.status == '未服务'"
                        size="small">
                        拒绝服务</el-button>
                    <el-button type="primary" @click="toAdd(scope.row)" v-if="scope.row.status == '待评价'" size="small">评价
                    </el-button>
                    <el-tag type="success" v-if="scope.row.status == '已完成'">已完成</el-tag>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog title="派单" :visible.sync="dialogVisible" width="35%">
            <el-form :model="form" label-width="100px">
                <el-form-item label="状态：">
                    <el-tag>{{ this.form.status }}</el-tag>
                </el-form-item>
                <el-form-item label="员工：">
                    <el-select v-model="form.employeeId" placeholder="请选择派单" style="width:400px">
                        <el-option v-for="e in employee" :key="e.id" :label="e.name" :value="e.id" v-if="e.status==='正常'">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="订单时间：">
                    <el-input v-model="form.orderTime" style="width:400px" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="总价：">
                    <el-input v-model="form.total" style="width:400px" :disabled="true">
                        <i slot="append">元</i>
                    </el-input>
                </el-form-item>
                <el-form-item align="right">
                    <el-button @click="close()">取 消</el-button>
                    <el-button type="primary" @click="sendOrder()">确 定</el-button>
                </el-form-item>
            </el-form>

        </el-dialog>

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
    </div>
</template>

<script>
import axios from 'axios';
import { getDate } from '@/utils/common'

export default {
    data() {
        return {
            dialogVisible: false,
            visible: false,
            param: {
                status: 'all'
            },
            orders: [],
            form: {},
            employee: [],
            comment: {
                id:'',
                userId:''
            },
        }
    },

    created() {
        this.loadData();
        this.loadEmployee();
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
            this.comment.userId=row.customerId;
            console.log(this.comment)
            this.visible = true;
        },

        // 关闭评价对话框
        toClose() {
            this.visible = false;
            this.comment = {};
        },

        // 打开派单对话框
        open(row) {
            this.form = row
            this.dialogVisible = true;
        },

        // 关闭派单对话框
        close() {
            this.dialogVisible = false;
            this.form = {};
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

        // 派单
        sendOrder() {
            axios({
                method: 'get',
                url: 'http://localhost:8080/order/sendOrder',
                params: {
                    id: this.form.id,
                    employeeId: this.form.employeeId
                }
            }).then((response) => {
                if (response.data.code === 200) {
                    // 成功弹窗
                    this.$message({
                        message: response.data.message,
                        type: 'success'
                    });
                    // 关闭对话框
                    this.close();
                    // 刷新
                    this.loadData();
                } else {
                    alert(response.data.message);
                }
            });
        },

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
            // 当status未all时，删除，使其查询所有订单信息
            if (param.status == 'all') {
                delete param.status
            }

            axios({
                method: 'get',
                url: 'http://localhost:8080/order/findByStatus',
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

        loadEmployee() {
            axios({
                method: 'get',
                url: 'http://localhost:8080/user/findAllEmployee'
            }).then((response) => {
                if (response.data.code === 200) {
                    this.employee = response.data.data;

                } else {
                    alert(response.data.message);
                }
            });
        }
    }
}

</script>

<style>
</style>