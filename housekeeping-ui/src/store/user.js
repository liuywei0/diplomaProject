import Cookie from "js-cookie"

export default {
    state: {
        token: ''
    },
    mutations: {
        setToken(state, val) {
            state.token = val;
            Cookie.set('token', val)
        },
        
        clearToken(state) {
            state.Cookie = '';
            Cookie.remove('token');
        },

        getToken(state) {
            state.token = state.token || Cookie.get('token')
        }
    }
}