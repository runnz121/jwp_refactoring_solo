package kitchenpos.menu.exception;

public class MenuPriceException extends RuntimeException{
    public MenuPriceException(MenuPriceExceptionType exceptionType){
        super(exceptionType.message);
    }
}
