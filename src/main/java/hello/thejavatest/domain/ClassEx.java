package hello.thejavatest.domain;

abstract class ClassEx {

    static final String 필수니 = "필수다";
    public String 필수일까 = "필수다";

    abstract void 메서드();

    abstract class abstractTestEx {

        abstract void abstractMethod2();

    }

    interface interfaceTestEx {
        void interfaceTestEx();
    }
    interface interfaceTestEx2 {
        void interfaceTestEx();
    }

    public class playClass extends abstractTestEx implements interfaceTestEx, interfaceTestEx2 {

        public void interfaceTestEx() {

        }


        void abstractMethod2() {
            System.out.println("");

        }
    }

}


