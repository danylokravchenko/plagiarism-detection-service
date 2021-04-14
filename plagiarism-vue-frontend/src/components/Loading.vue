<template>
    <div class="d-flex justify-content-center" id="loading">
        <div class="spinner-grow text-success loading" role="status"></div>
        <div class="spinner-grow text-success loading" role="status"></div>
        <div class="spinner-grow text-success loading" role="status"></div>
        <div class="spinner-grow text-success loading" role="status"></div>
        <div class="spinner-grow text-success loading" role="status"></div>
    </div>
</template>

<script>
    export default {
        name: "Loading"
    }
</script>

<style scoped>
    .loading{
        margin: 0 10px 0 10px;
    }
</style>