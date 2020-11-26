package com.example.alcoholic.http.response;

/**
 * Created by
 * Description:图片上传返回实体类
 * on 2020/11/23.
 */
public class ImgUploadBean {


    /**
     * data : {"id":"gP1ZDr7","title":"img-7833e84d85da4ef19fa268d78e5e1908","url_viewer":"https://ibb.co/gP1ZDr7","url":"https://i.ibb.co/9VXHqpv/img-7833e84d85da4ef19fa268d78e5e1908.jpg","display_url":"https://i.ibb.co/txfpbHh/img-7833e84d85da4ef19fa268d78e5e1908.jpg","size":35907,"time":"1606110231","expiration":"0","image":{"filename":"img-7833e84d85da4ef19fa268d78e5e1908.jpg","name":"img-7833e84d85da4ef19fa268d78e5e1908","mime":"image/jpeg","extension":"jpg","url":"https://i.ibb.co/9VXHqpv/img-7833e84d85da4ef19fa268d78e5e1908.jpg"},"thumb":{"filename":"img-7833e84d85da4ef19fa268d78e5e1908.jpg","name":"img-7833e84d85da4ef19fa268d78e5e1908","mime":"image/jpeg","extension":"jpg","url":"https://i.ibb.co/gP1ZDr7/img-7833e84d85da4ef19fa268d78e5e1908.jpg"},"medium":{"filename":"img-7833e84d85da4ef19fa268d78e5e1908.jpg","name":"img-7833e84d85da4ef19fa268d78e5e1908","mime":"image/jpeg","extension":"jpg","url":"https://i.ibb.co/txfpbHh/img-7833e84d85da4ef19fa268d78e5e1908.jpg"},"delete_url":"https://ibb.co/gP1ZDr7/96cda5476745b08f66baaa8b26bd6e0a"}
     * success : true
     * status : 200
     */

    private DataBean data;
    private boolean success;
    private int status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * id : gP1ZDr7
         * title : img-7833e84d85da4ef19fa268d78e5e1908
         * url_viewer : https://ibb.co/gP1ZDr7
         * url : https://i.ibb.co/9VXHqpv/img-7833e84d85da4ef19fa268d78e5e1908.jpg
         * display_url : https://i.ibb.co/txfpbHh/img-7833e84d85da4ef19fa268d78e5e1908.jpg
         * size : 35907
         * time : 1606110231
         * expiration : 0
         * image : {"filename":"img-7833e84d85da4ef19fa268d78e5e1908.jpg","name":"img-7833e84d85da4ef19fa268d78e5e1908","mime":"image/jpeg","extension":"jpg","url":"https://i.ibb.co/9VXHqpv/img-7833e84d85da4ef19fa268d78e5e1908.jpg"}
         * thumb : {"filename":"img-7833e84d85da4ef19fa268d78e5e1908.jpg","name":"img-7833e84d85da4ef19fa268d78e5e1908","mime":"image/jpeg","extension":"jpg","url":"https://i.ibb.co/gP1ZDr7/img-7833e84d85da4ef19fa268d78e5e1908.jpg"}
         * medium : {"filename":"img-7833e84d85da4ef19fa268d78e5e1908.jpg","name":"img-7833e84d85da4ef19fa268d78e5e1908","mime":"image/jpeg","extension":"jpg","url":"https://i.ibb.co/txfpbHh/img-7833e84d85da4ef19fa268d78e5e1908.jpg"}
         * delete_url : https://ibb.co/gP1ZDr7/96cda5476745b08f66baaa8b26bd6e0a
         */

        private String id;
        private String title;
        private String url_viewer;
        private String url;
        private String display_url;
        private int size;
        private String time;
        private String expiration;
        private ImageBean image;
        private ThumbBean thumb;
        private MediumBean medium;
        private String delete_url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl_viewer() {
            return url_viewer;
        }

        public void setUrl_viewer(String url_viewer) {
            this.url_viewer = url_viewer;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDisplay_url() {
            return display_url;
        }

        public void setDisplay_url(String display_url) {
            this.display_url = display_url;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getExpiration() {
            return expiration;
        }

        public void setExpiration(String expiration) {
            this.expiration = expiration;
        }

        public ImageBean getImage() {
            return image;
        }

        public void setImage(ImageBean image) {
            this.image = image;
        }

        public ThumbBean getThumb() {
            return thumb;
        }

        public void setThumb(ThumbBean thumb) {
            this.thumb = thumb;
        }

        public MediumBean getMedium() {
            return medium;
        }

        public void setMedium(MediumBean medium) {
            this.medium = medium;
        }

        public String getDelete_url() {
            return delete_url;
        }

        public void setDelete_url(String delete_url) {
            this.delete_url = delete_url;
        }

        public static class ImageBean {
            /**
             * filename : img-7833e84d85da4ef19fa268d78e5e1908.jpg
             * name : img-7833e84d85da4ef19fa268d78e5e1908
             * mime : image/jpeg
             * extension : jpg
             * url : https://i.ibb.co/9VXHqpv/img-7833e84d85da4ef19fa268d78e5e1908.jpg
             */

            private String filename;
            private String name;
            private String mime;
            private String extension;
            private String url;

            public String getFilename() {
                return filename;
            }

            public void setFilename(String filename) {
                this.filename = filename;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMime() {
                return mime;
            }

            public void setMime(String mime) {
                this.mime = mime;
            }

            public String getExtension() {
                return extension;
            }

            public void setExtension(String extension) {
                this.extension = extension;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class ThumbBean {
            /**
             * filename : img-7833e84d85da4ef19fa268d78e5e1908.jpg
             * name : img-7833e84d85da4ef19fa268d78e5e1908
             * mime : image/jpeg
             * extension : jpg
             * url : https://i.ibb.co/gP1ZDr7/img-7833e84d85da4ef19fa268d78e5e1908.jpg
             */

            private String filename;
            private String name;
            private String mime;
            private String extension;
            private String url;

            public String getFilename() {
                return filename;
            }

            public void setFilename(String filename) {
                this.filename = filename;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMime() {
                return mime;
            }

            public void setMime(String mime) {
                this.mime = mime;
            }

            public String getExtension() {
                return extension;
            }

            public void setExtension(String extension) {
                this.extension = extension;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class MediumBean {
            /**
             * filename : img-7833e84d85da4ef19fa268d78e5e1908.jpg
             * name : img-7833e84d85da4ef19fa268d78e5e1908
             * mime : image/jpeg
             * extension : jpg
             * url : https://i.ibb.co/txfpbHh/img-7833e84d85da4ef19fa268d78e5e1908.jpg
             */

            private String filename;
            private String name;
            private String mime;
            private String extension;
            private String url;

            public String getFilename() {
                return filename;
            }

            public void setFilename(String filename) {
                this.filename = filename;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMime() {
                return mime;
            }

            public void setMime(String mime) {
                this.mime = mime;
            }

            public String getExtension() {
                return extension;
            }

            public void setExtension(String extension) {
                this.extension = extension;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
