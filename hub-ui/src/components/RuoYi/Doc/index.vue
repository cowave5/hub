<template>
  <div>
    <svg-icon icon-class="question" @click="goto" />
  </div>
</template>

<script>
export default {
  name: 'hub-ui',
  data() {
    return {
      url: 'https://github.com/cowave5/hub'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
