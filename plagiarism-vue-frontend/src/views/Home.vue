<template>
  <div class="container">
    <b-jumbotron header="plagiarism.io" lead="Plagiarism detection service" style="margin-top: 20px; background: #fafafa;">
      <p>Try it quick by pressing the button</p>
      <router-link class="btn btn-outline-success me-2" type="button" to="/detection" style="width: 100%; margin-top: 20px;">Try it for free</router-link>
    </b-jumbotron>

    <marquee behavior="alternate" direction="right"><h4><span v-for="i in 20">plagiarism<span style="color: green">.io </span> </span></h4> </marquee>
<!--    <h2>Some info about site</h2>-->
    <b-carousel
            id="carousel-1"
            v-model="slide"
            :interval="4000"
            controls
            indicators
            background="#ababab"
            img-width="1024"
            img-height="480"
            style="text-shadow: 1px 1px 2px #333; margin-top: 20px;"
            @sliding-start="onSlideStart"
            @sliding-end="onSlideEnd"
    >
      <!-- Text slides with image -->
      <b-carousel-slide img-src="1.png"></b-carousel-slide>
      <b-carousel-slide img-src="2.png"></b-carousel-slide>
      <b-carousel-slide img-src="3.png"></b-carousel-slide>


    </b-carousel>

    <router-link class="btn btn-outline-success me-2" type="button" to="/detection" style="width: 100%; margin-top: 20px;">Try it for free</router-link>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        slide: 0,
        sliding: null
      }
    },
    methods: {
      onSlideStart(slide) {
        this.sliding = true
      },
      onSlideEnd(slide) {
        this.sliding = false
      }
    }
  }
</script>
