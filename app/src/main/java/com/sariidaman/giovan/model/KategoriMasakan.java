package com.sariidaman.giovan.model;

import java.io.Serializable;

public class KategoriMasakan implements Serializable {
        public String id;
        public String nama;

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        public String getNama() {
                return nama;
        }

        public void setNama(String nama) {
                this.nama = nama;
        }
}
