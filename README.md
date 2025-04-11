<div align="center">
    <h2>Kompresi Gambar dengan Quadtree</h2>
    <p>Tugas Kecil 2 IF2211 Strategi Algoritma</p>
    <br/>
</div>

## Deskripsi Program
Quadtree adalah salah satu struktur data yang dimanfaatkan untuk image compression. Quadtree memiliki struktur yang sama seperti struktur pohon (tree), namun memiliki 4 anak (child) dibandingkan 2 pada umumnya – jika berbicara tentang binary tree –. Dalam konteks image compression, quadtree dapat membagi gambar menjadi blok-blok yang lebih kecil berdasarkan keseragaman warna atau intensitas pixel. Struktur ini memungkinkan pengkodean data gambar yang lebih efisien dengan menghilangkan redundansi pada area yang seragam. QuadTree sering digunakan dalam algoritma kompresi lossy karena mampu mengurangi ukuran file secara signifikan tanpa mengorbankan detail penting pada gambar.


## Requirement Program dan Instalasi Tertentu
Program ini dapat dijalankan dalam terminal lokal(cmd) atau VSCode. Pastikan bahasa Java sudah diunduh di sistem anda.

## Cara Mengkompilasi & Menjalankan Program
**1.** Buka link repository GitHub, kemudian salin URL di bagian "Code" untuk melakukan git clone

**2.** Buka terminal di VSCode atau CMD

**3.** Arahkan directory ke folder tempat anda ingin melakukan git clone

**4.** Perintahkan git clone
```
git clone https://github.com/andhikalucas/Tucil2_13523014.git
```
**5.** Setelah berhasil, arahkan directory ke folder src
```
cd src
```
**6.** Kompilasikan program program utama dengan perintah berikut:
```
javac -d ../bin *.java
```
**7.** Setelah berhasil, pindah directory ke bin
```
cd ../bin
```
**8.** Jalankan Main dengan perintah
```
java Main
```

## Author
| NIM | Nama |
| :---: | :---: |
| 13523014 | Nicholas Andhika Lucas |