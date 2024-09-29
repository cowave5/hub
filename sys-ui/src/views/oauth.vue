<script>
export default {
  created() {
    const { params, query } = this.$route
    this.$store.dispatch("OauthGitlab", query.code).then(async () => {
      this.$router.push({path: this.redirect || "/"}).catch(() => {});
      await this.$store.dispatch('OpenNoticeSocket');
    }).catch(() => {});
  },
  render: function(h) {

  },
}
</script>
