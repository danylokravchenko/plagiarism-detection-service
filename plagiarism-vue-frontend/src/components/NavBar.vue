<template>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container">
            <span class="navbar-brand">plagiarism<span style="color: green;">.io</span></span>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                    aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <router-link class="nav-link" to="/">Home</router-link>
                    </li>
                    <li class="nav-item">
                        <router-link class="nav-link" to="/detection">Detection
                        </router-link>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="https://github.com/UndeadBigUnicorn/plagiarism#the-project">About</a>
                    </li>
<!--                    <li class="nav-item">-->
<!--                        <router-link class="nav-link" to="/api/docs">API Docs</router-link>-->
<!--                    </li>-->
                </ul>
            </div>
            <router-link v-if="!isLogin" class="btn btn-outline-success me-2" type="button" to="/login">Login</router-link>
            <router-link v-if="isLogin" class="btn btn-success me-2" type="button" to="/profile" style="margin-right: 10px;">{{username}}</router-link>
            <router-link v-if="isLogin" class="btn btn-outline-success me-2" type="button" to="/logout">Logout</router-link>
        </div>
    </nav>
</template>

<script>
    import {authService} from '../services/authService';

    export default {
        name: "NavBar",
        data(){
            return{
                isLogin: Boolean(authService.getUser()),
                username: authService.getUser() ? authService.getUser().username : ''
            }
        },
        created() {
            this.$bus.$on('logged', () => {
                this.isLogin = Boolean(authService.getUser())
                this.username = this.isLogin ? authService.getUser().username : ''
            })
        }
    }
</script>

<style scoped>
    .nav-link .active{
        color: green !important;
        font-weight: 500;
    }
</style>