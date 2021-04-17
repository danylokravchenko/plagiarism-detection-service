<template>
    <div class="container login">
        <div class="container col-lg-7">
            <div class="card">
                <div class="card-body">
                    <h2 class="card-title">Log in</h2>
                    <p class="card-text">to collect detection statistic and have access to API</p>
                    <div class="alert alert-danger" role="alert" v-if="error">
                        {{error}}
                    </div>
                    <form>
                        <div class="mb-3">
                            <label for="exampleInputEmail1" class="form-label">Email address</label>
                            <input class="form-control" v-model="username" id="exampleInputEmail1"
                                   aria-describedby="emailHelp">
                        </div>
                        <div class="mb-3">
                            <label for="exampleInputPassword1" class="form-label">Password</label>
                            <input type="password" class="form-control" v-model="password" id="exampleInputPassword1">
                        </div>
                        <div class="row align-items-start">
                            <div class="col">
                                <button type="button" class="btn btn-success full-width" v-on:click="submitLogIn()">Log
                                    in
                                </button>
                            </div>
                            <div class="col">
                                <router-link type="submit" class="btn btn-outline-success full-width" to="/register">
                                    Register
                                </router-link>
                            </div>
                        </div>
                    </form>
                </div>

            </div>

        </div>
    </div>
</template>

<script>


    import {authService} from "../services/authService";

    export default {
        data() {
            return {
                username: '',
                password: '',
                error: ''
            }
        },
        methods: {
            submitLogIn() {
                this.axios
                    .post('http://localhost:8080/auth/login', {
                        login: this.username,
                        password: this.password
                    })
                    .then(response => {
                        console.log(response)
                        let user = {
                            id: response.data.id,
                            username: response.data.login,
                            token: response.headers.authorization.split(' ')[1]
                        }

                        authService.auth(user);
                        this.$bus.$emit('logged', 'User logged');
                        this.$router.push('/');
                    })
                    .catch(error => {
                        this.error = error.response.data.message
                    })
            }

        },
        created() {
            authService.logout();
            this.$bus.$emit('logged', 'User logged out');
        }
    }
</script>

<style scoped>
    .card {
        margin-top: 50px;
    }

    .full-width {
        width: 100%;
    }
</style>