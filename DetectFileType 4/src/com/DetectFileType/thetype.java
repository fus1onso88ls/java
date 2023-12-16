package com.DetectFileType;

    public class thetype {
        private byte[] bytes;//массив байтов
        private boolean[] checkbytes;//массив true - проверять байт false нет
        private int bytesize;//размер массива байтов
        public int typenum;//количество возможных расширений
        public String typename;//имя расширений (через пробел если несколько)
        public thetype(String typename, byte... bytes) {
            this.bytes = new byte[bytes.length];
            this.checkbytes = new boolean[bytes.length];
            for (int i =0;i<bytes.length;i++){
                this.bytes[i]=bytes[i];
                this.checkbytes[i]=true;
            }
            this.bytesize = bytes.length;
            this.typename = typename;
            this.typenum = 0;
            for (int j = 0; j < this.typename.length(); j++)
            {
                if (this.typename.charAt(j) == '.') {
                    this.typenum++;
                }
            }
        }
        public thetype(String S){
            int i=S.indexOf('.');
            this.typename=S.substring(i);
            this.typenum = 0;
            for (int j = 0; j < this.typename.length(); j++)
            {
                if (this.typename.charAt(j) == '.') {
                    this.typenum++;
                }
            }
            String strbytes=S.substring(0,i);
            strbytes = strbytes.replaceAll(" ","");
            int n=strbytes.length()/2;
            this.bytesize = n;
            this.bytes = new byte[n];
            this.checkbytes = new boolean[n];
            i=0;
            for (int j = 0; j < strbytes.length()-1; j+=2) {
                if (strbytes.charAt(j)=='x'||strbytes.charAt(j+1)=='x'){
                    this.bytes[i]=0;
                    this.checkbytes[i]=false;
                }
                else {
                    String Shex=String.valueOf(strbytes.charAt(j))+String.valueOf(strbytes.charAt(j+1));
                    this.bytes[i]=HexToByte(Shex);
                    this.checkbytes[i]=true;
                }
                i++;
            }
        }
        public void uncheckbytes(int... bytes) {
            for (int i =0;i<bytes.length;i++){
                int k=bytes[i];
                if (k<this.bytesize) {
                    this.checkbytes[k] = false;
                }
            }
        }
        public boolean checkbuffer(byte[] buf) {
            boolean matched=true;
            for(int j=0;matched && j<this.bytesize;j++){
                if (this.checkbytes[j] && this.bytes[j]!=buf[j]){
                    matched=false;
                }
            }
            return matched;
        }
        public String savebase(){
            String Hex;
            String S = "";
            for(int j=0;j<this.bytesize;j++){
                if (this.checkbytes[j]) {
                    Hex = Integer.toHexString(this.bytes[j]);
                    if (Hex.length() > 2) {
                        Hex=Hex.substring(Hex.length()-2);
                    }
                    else if (Hex.length() == 1) {
                        Hex = "0" + Hex;
                    }
                }
                else {
                    Hex = "xx";
                }
                S = S + Hex+" ";
            }
            return S+this.typename;
        }
        public static boolean checkloadbase(String S){
            int i=S.indexOf('.');
            if (i==-1){
                return false;
            }
            String strbytes=S.substring(0,i);
            strbytes = strbytes.replaceAll(" ","");
            if (strbytes.length()==0){
                return false;
            }
            if (strbytes.length()%2!=0){
                return false;
            }

            if (!strbytes.matches("[0-9abcdefx]+")){
                return false;
            }
            return true;
        }
        private static byte HexToByte(String data) {
            return (byte) ((Character.digit(data.charAt(0), 16) << 4) | Character.digit(data.charAt(1), 16));
        }
    }

