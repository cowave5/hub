<template>
  <div>
    <canvas ref="qrcodeCanvas"></canvas>
  </div>
</template>

<script>
import QRCode from 'qrcode'

export default {
  name: 'MfaQrcode',
  props: {
    otpAuthUrl: {
      type: String,
      required: true
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.generateQRCode()
    })
  },
  methods: {
    async generateQRCode() {
      try {
        await QRCode.toCanvas(this.$refs.qrcodeCanvas, this.otpAuthUrl, {
          width: 200
        })
      } catch (err) {
        console.error('二维码生成失败', err)
      }
    }
  }
}
</script>
