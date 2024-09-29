<template>
  <i-frame :src="url" />
</template>
<script>
import iFrame from "@/components/iFrame/index";
export default {
  name: "monitor-alert",
  components: { iFrame },
  data() {
    let url = '';
      if (process.env.NODE_ENV === 'production') {
        url = 'http://' + location.hostname +':86';
      }else{
        url = 'http://10.64.4.74:9083';
      }
    return {
        url
    };
  },
};
</script>
