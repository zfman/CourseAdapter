package com.zhuangfei.adapterlib.qingguo.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/* compiled from: Md5 */
public class Md5 {
    private static byte[] a = new byte[]{Byte.MIN_VALUE, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
    private InputStream b;
    private boolean c;
    private int[] d;
    private long e;
    private byte[] f;
    private byte[] g;

    public static String a(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer(bArr.length * 2);
        for (int i = 0; i < bArr.length; i++) {
            int i2 = (bArr[i] & 240) >> 4;
            int i3 = bArr[i] & 15;
            stringBuffer.append(new Character((char) (i2 > 9 ? (i2 + 97) - 10 : i2 + 48)));
            stringBuffer.append(new Character((char) (i3 > 9 ? (i3 + 97) - 10 : i3 + 48)));
        }
        return stringBuffer.toString();
    }

    private final int a(int i, int i2, int i3) {
        return (i & i2) | ((i ^ -1) & i3);
    }

    private final int b(int i, int i2, int i3) {
        return (i & i3) | ((i3 ^ -1) & i2);
    }

    private final int c(int i, int i2, int i3) {
        return (i ^ i2) ^ i3;
    }

    private final int d(int i, int i2, int i3) {
        return ((i3 ^ -1) | i) ^ i2;
    }

    private final int a(int i, int i2) {
        return (i << i2) | (i >>> (32 - i2));
    }

    private final int a(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        return a(((a(i2, i3, i4) + i5) + i7) + i, i6) + i2;
    }

    private final int b(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        return a(((b(i2, i3, i4) + i5) + i7) + i, i6) + i2;
    }

    private final int c(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        return a(((c(i2, i3, i4) + i5) + i7) + i, i6) + i2;
    }

    private final int d(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        return a(((d(i2, i3, i4) + i5) + i7) + i, i6) + i2;
    }

    private final void a(int[] iArr, byte[] bArr, int i, int i2) {
        int i3 = 0;
        int i4 = 0;
        while (i3 < i2) {
            iArr[i4] = (((bArr[i + i3] & 255) | ((bArr[(i + i3) + 1] & 255) << 8)) | ((bArr[(i + i3) + 2] & 255) << 16)) | ((bArr[(i + i3) + 3] & 255) << 24);
            i4++;
            i3 += 4;
        }
    }

    private final void a(byte[] bArr, int i) {
        int i2 = this.d[0];
        int i3 = this.d[1];
        int i4 = this.d[2];
        int i5 = this.d[3];
        int[] iArr = new int[16];
        a(iArr, bArr, i, 64);
        int a = a(i2, i3, i4, i5, iArr[0], 7, -680876936);
        int a2 = a(i5, a, i3, i4, iArr[1], 12, -389564586);
        int a3 = a(i4, a2, a, i3, iArr[2], 17, 606105819);
        int a4 = a(i3, a3, a2, a, iArr[3], 22, -1044525330);
        int a5 = a(a, a4, a3, a2, iArr[4], 7, -176418897);
        int a6 = a(a2, a5, a4, a3, iArr[5], 12, 1200080426);
        i3 = a(a3, a6, a5, a4, iArr[6], 17, -1473231341);
        int a7 = a(a4, i3, a6, a5, iArr[7], 22, -45705983);
        int a8 = a(a5, a7, i3, a6, iArr[8], 7, 1770035416);
        a4 = a(a6, a8, a7, i3, iArr[9], 12, -1958414417);
        i3 = a(i3, a4, a8, a7, iArr[10], 17, -42063);
        int a9 = a(a7, i3, a4, a8, iArr[11], 22, -1990404162);
        int a10 = a(a8, a9, i3, a4, iArr[12], 7, 1804603682);
        a7 = a(a4, a10, a9, i3, iArr[13], 12, -40341101);
        i3 = a(i3, a7, a10, a9, iArr[14], 17, -1502002290);
        a3 = a(a9, i3, a7, a10, iArr[15], 22, 1236535329);
        a10 = b(a10, a3, i3, a7, iArr[1], 5, -165796510);
        i4 = b(a7, a10, a3, i3, iArr[6], 9, -1069501632);
        a2 = b(i3, i4, a10, a3, iArr[11], 14, 643717713);
        a9 = b(a3, a2, i4, a10, iArr[0], 20, -373897302);
        i5 = b(a10, a9, a2, i4, iArr[5], 5, -701558691);
        a = b(i4, i5, a9, a2, iArr[10], 9, 38016083);
        int b = b(a2, a, i5, a9, iArr[15], 14, -660478335);
        a3 = b(a9, b, a, i5, iArr[4], 20, -405537848);
        a6 = b(i5, a3, b, a, iArr[9], 5, 568446438);
        int b2 = b(a, a6, a3, b, iArr[14], 9, -1019803690);
        a2 = b(b, b2, a6, a3, iArr[3], 14, -187363961);
        a5 = b(a3, a2, b2, a6, iArr[8], 20, 1163531501);
        int b3 = b(a6, a5, a2, b2, iArr[13], 5, -1444681467);
        a = b(b2, b3, a5, a2, iArr[2], 9, -51403784);
        a4 = b(a2, a, b3, a5, iArr[7], 14, 1735328473);
        int b4 = b(a5, a4, a, b3, iArr[12], 20, -1926607734);
        a6 = c(b3, b4, a4, a, iArr[5], 4, -378558);
        a8 = c(a, a6, b4, a4, iArr[8], 11, -2022574463);
        int c = c(a4, a8, a6, b4, iArr[11], 16, 1839030562);
        a5 = c(b4, c, a8, a6, iArr[14], 23, -35309556);
        a7 = c(a6, a5, c, a8, iArr[1], 4, -1530992060);
        int c2 = c(a8, a7, a5, c, iArr[4], 11, 1272893353);
        a4 = c(c, c2, a7, a5, iArr[7], 16, -155497632);
        a10 = c(a5, a4, c2, a7, iArr[10], 23, -1094730640);
        int c3 = c(a7, a10, a4, c2, iArr[13], 4, 681279174);
        a8 = c(c2, c3, a10, a4, iArr[0], 11, -358537222);
        a9 = c(a4, a8, c3, a10, iArr[3], 16, -722521979);
        int c4 = c(a10, a9, a8, c3, iArr[6], 23, 76029189);
        a7 = c(c3, c4, a9, a8, iArr[9], 4, -640364487);
        b = c(a8, a7, c4, a9, iArr[12], 11, -421815835);
        int c5 = c(a9, b, a7, c4, iArr[15], 16, 530742520);
        a10 = c(c4, c5, b, a7, iArr[2], 23, -995338651);
        b2 = d(a7, a10, c5, b, iArr[0], 6, -198630844);
        int d = d(b, b2, a10, c5, iArr[7], 10, 1126891415);
        a9 = d(c5, d, b2, a10, iArr[14], 15, -1416354905);
        b3 = d(a10, a9, d, b2, iArr[5], 21, -57434055);
        int d2 = d(b2, b3, a9, d, iArr[12], 6, 1700485571);
        b = d(d, d2, b3, a9, iArr[3], 10, -1894986606);
        b4 = d(a9, b, d2, b3, iArr[10], 15, -1051523);
        int d3 = d(b3, b4, b, d2, iArr[1], 21, -2054922799);
        b2 = d(d2, d3, b4, b, iArr[8], 6, 1873313359);
        c = d(b, b2, d3, b4, iArr[15], 10, -30611744);
        int d4 = d(b4, c, b2, d3, iArr[6], 15, -1560198380);
        b3 = d(d3, d4, c, b2, iArr[13], 21, 1309151649);
        c2 = d(b2, b3, d4, c, iArr[4], 6, -145523070);
        int d5 = d(c, c2, b3, d4, iArr[11], 10, -1120210379);
        b4 = d(d4, d5, c2, b3, iArr[2], 15, 718787259);
        int d6 = d(b3, b4, d5, c2, iArr[9], 21, -343485551);
        int[] iArr2 = this.d;
        iArr2[0] = iArr2[0] + c2;
        iArr2 = this.d;
        iArr2[1] = d6 + iArr2[1];
        int[] iArr3 = this.d;
        iArr3[2] = iArr3[2] + b4;
        iArr3 = this.d;
        iArr3[3] = iArr3[3] + d5;
    }

    private final void b(byte[] bArr, int i) {
        int i2 = 0;
        int i3 = ((int) (this.e >> 3)) & 63;
        this.e += (long) (i << 3);
        int i4 = 64 - i3;
        if (i >= i4) {
            System.arraycopy(bArr, 0, this.f, i3, i4);
            a(this.f, 0);
            i3 = i4;
            while (i3 + 63 < i) {
                a(bArr, i3);
                i3 += 64;
            }
        } else {
            i2 = i3;
            i3 = 0;
        }
        System.arraycopy(bArr, i3, this.f, i2, i - i3);
    }

    private byte[] b() {
        int i;
        byte[] bArr = new byte[8];
        for (i = 0; i < 8; i++) {
            bArr[i] = (byte) ((int) ((this.e >>> (i * 8)) & 255));
        }
        i = ((int) (this.e >> 3)) & 63;
        b(a, i < 56 ? 56 - i : 120 - i);
        b(bArr, 8);
        return a(this.d, 16);
    }

    private byte[] a(int[] iArr, int i) {
        int i2 = 0;
        byte[] bArr = new byte[i];
        int i3 = 0;
        while (i2 < i) {
            bArr[i2] = (byte) (iArr[i3] & 255);
            bArr[i2 + 1] = (byte) ((iArr[i3] >> 8) & 255);
            bArr[i2 + 2] = (byte) ((iArr[i3] >> 16) & 255);
            bArr[i2 + 3] = (byte) ((iArr[i3] >> 24) & 255);
            i3++;
            i2 += 4;
        }
        return bArr;
    }

    public byte[] a() throws IOException {
        byte[] bArr = new byte[1024];
        if (this.g != null) {
            return this.g;
        }
        while (true) {
            int read = this.b.read(bArr);
            if (read > 0) {
                b(bArr, read);
            } else {
                this.g = b();
                return this.g;
            }
        }
    }

    public Md5(String str, String str2) {
        this.b = null;
        this.c = false;
        this.d = null;
        this.e = 0;
        this.f = null;
        this.g = null;
        try {
            byte[] bytes = str.getBytes(str2);
            this.c = true;
            this.b = new ByteArrayInputStream(bytes);
            this.d = new int[4];
            this.f = new byte[64];
            this.e = 0;
            this.d[0] = 1732584193;
            this.d[1] = -271733879;
            this.d[2] = -1732584194;
            this.d[3] = 271733878;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("no " + str2 + " encoding!!!");
        }
    }

    public Md5(String str) {
        this(str, "UTF8");
    }
}
