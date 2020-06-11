package com.smart.basic;

public class SequenceNumber {
    private static ThreadLocal<Integer> seqNum = new ThreadLocal<Integer>(){
        public Integer initialValue(){
            return 0;
        }
    };

    public int getNextNum(){
        seqNum.set(seqNum.get()+1);
        return seqNum.get();
    }

    public static void main(String[] args) {
        SequenceNumber seqNum = new SequenceNumber();
        TestClient t1 = new TestClient(seqNum);
        TestClient t2 = new TestClient(seqNum);
        TestClient t3 = new TestClient(seqNum);
        t1.start();
        t2.start();
        t3.start();
    }

    private static class TestClient extends Thread{
        private SequenceNumber sn;
        public TestClient(SequenceNumber sn){
            this.sn = sn;
        }

        public void run(){
            for(int i = 0; i<3; i++){
                System.out.println("thread["+Thread.currentThread().getName()+"] SN[" +sn.getNextNum()+ "]");
            }
        }

    }
}
