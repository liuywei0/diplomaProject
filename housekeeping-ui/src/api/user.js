import request from '@/utils/request'
import axios from 'axios';

export function login(data) {
  return axios.post("http://localhost:8080/user/login", data)
  // return request({
  //   url: '/user/login',
  //   method: 'post',
  //   data
  // })
}

export function getInfo(token) {
  // console.log(token)
  return axios.get("http://localhost:8080/user/info?token="+token)
  // return request({
  //   url: '/user/getInfo',
  //   method: 'get',
  //   params: { token }
  // })
}

export function logout() {
  return axios.get("http://localhost:8080/user/logout")
}
