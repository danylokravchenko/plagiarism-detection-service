<template>
    <div id="app">
        <NavBar></NavBar>
        <router-view/>
        <Footer></Footer>
    </div>
</template>

<script>
    import NavBar from "./components/NavBar";
    import Footer from "./components/Footer";
    export default {
        components: {Footer, NavBar}
    }
</script>

<style>

</style>