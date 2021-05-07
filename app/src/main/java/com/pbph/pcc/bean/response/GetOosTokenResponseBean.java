package com.pbph.pcc.bean.response;

/**
 * Created by Administrator on 2017/9/26.
 */

public class GetOosTokenResponseBean extends BaseResponseBean {

    /**
     * code : 200
     * data : {"accessKeyId":"STS.Lj5o518CrXNjozGQLoJLsuGGs","accessKeySecret":"BahYc9XDTbwUwFUY3N5ZXbeLcGWtJQKY7e5ES93SBKXJ","expiration":"2017-09-26T06:53:49Z","securityToken":"CAIS8QF1q6Ft5B2yfSjIramAJI+F1ZxT74yBbVz2tUw6RsNfmoLskTz2IH9IenhhAO0Wv/ozmWBQ6P4blqB6T55OSAmcNZIoDlTlfoDiMeT7oMWQweEuqv/MQBq+aXPS2MvVfJ+KLrf0ceusbFbpjzJ6xaCAGxypQ12iN+/i6/clFKN1ODO1dj1bHtxbCxJ/ocsBTxvrOO2qLwThjxi7biMqmHIl1Tsks/Xin5zEtkCF1AOg8IJP+dSteKrDRtJ3IZJyX+2y2OFLbafb2EZSkUMVqPkn3fUZqWaZ4ozNWwcJug/nNPHP+9luPRJ/dlbmyX+PTS+WGoABDN9m3B7MPiWyioh14vkAfISoLTUe73CtOsELigezOiOkL9g/MxA0nutxPFevf9ymyR8yZQxke1BeGPyHbL1NTmg5zVBA1nUmdBI+8NueZJ4ayJbfhPQ1XlM61q3pSJbbow6Shk1g6NB2ewxA0pO1153V6QPgQZM293w7NM2yk2c="}
     * msg : 成功
     */


    private DataBean data;


    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean {
        /**
         * accessKeyId : STS.Lj5o518CrXNjozGQLoJLsuGGs
         * accessKeySecret : BahYc9XDTbwUwFUY3N5ZXbeLcGWtJQKY7e5ES93SBKXJ
         * expiration : 2017-09-26T06:53:49Z
         * securityToken : CAIS8QF1q6Ft5B2yfSjIramAJI+F1ZxT74yBbVz2tUw6RsNfmoLskTz2IH9IenhhAO0Wv/ozmWBQ6P4blqB6T55OSAmcNZIoDlTlfoDiMeT7oMWQweEuqv/MQBq+aXPS2MvVfJ+KLrf0ceusbFbpjzJ6xaCAGxypQ12iN+/i6/clFKN1ODO1dj1bHtxbCxJ/ocsBTxvrOO2qLwThjxi7biMqmHIl1Tsks/Xin5zEtkCF1AOg8IJP+dSteKrDRtJ3IZJyX+2y2OFLbafb2EZSkUMVqPkn3fUZqWaZ4ozNWwcJug/nNPHP+9luPRJ/dlbmyX+PTS+WGoABDN9m3B7MPiWyioh14vkAfISoLTUe73CtOsELigezOiOkL9g/MxA0nutxPFevf9ymyR8yZQxke1BeGPyHbL1NTmg5zVBA1nUmdBI+8NueZJ4ayJbfhPQ1XlM61q3pSJbbow6Shk1g6NB2ewxA0pO1153V6QPgQZM293w7NM2yk2c=
         */

        private String accessKeyId;
        private String accessKeySecret;
        private String expiration;
        private String securityToken;

        public String getAccessKeyId() {
            return accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        public String getAccessKeySecret() {
            return accessKeySecret;
        }

        public void setAccessKeySecret(String accessKeySecret) {
            this.accessKeySecret = accessKeySecret;
        }

        public String getExpiration() {
            return expiration;
        }

        public void setExpiration(String expiration) {
            this.expiration = expiration;
        }

        public String getSecurityToken() {
            return securityToken;
        }

        public void setSecurityToken(String securityToken) {
            this.securityToken = securityToken;
        }
    }
}
