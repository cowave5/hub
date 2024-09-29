<template>
  <div>
    <svg-icon icon-class="github" @click="goto" />
  </div>
</template>

<script>
export default {
  name: 'sys-ui',
  data() {
    return {
      url: 'https://github.com/cowave5/sys'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
