<script>
export default {
  created() {
    const { params, query } = this.$route
     query.tenantId = "cowave";
    this.$store.dispatch("OauthGitlab", query).then(async () => {
      this.$router.push({path: this.redirect || "/"}).catch(() => {});
      await this.$store.dispatch('OpenNoticeSocket');
    }).catch(() => {});
  },
  render: function(h) {

  },
}
</script>
