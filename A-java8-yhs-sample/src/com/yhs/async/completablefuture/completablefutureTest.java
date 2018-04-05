package com.yhs.async.completablefuture;

import java.util.concurrent.CompletableFuture;

import org.junit.Test;


/**
 * java ���� �����ϴ� Future interface �� ������ Async ������ get() �޼���� Blocking �� �����ϴ� ����
 * Spring Framework���� �����ϴ� LinstenableFuture �� �ؼ� �� �� �־���.  (Spring Framework�� ����ϴ� �����ڶ�� RestTemplated �� Async ������ AsyncRestTemplate�� ���� ������ ���� ���� �ٶ���.)
 * ������ ListenableFuture�� �� �� �����ڶ�� �ѹ����� Callback �ۼ��� �����Կ� ����� ���� ���̴�. ������ Callback Hell �̶�� ���� Ŀ�´�Ƽ���� ���� ���� 
 * �� �������� �˾Ҵ��� Java 8 ���� ���� CompletableFuture �� �����Ѵ�. CompletableFuture�� �Ʒ��� ���� �������� ������ �����Ͽ� �񵿱� ���� �ڵ��� �������� ��������.
 */
public class completablefutureTest {
	 Runnable task = () -> {
	        try {
	            Thread.sleep(5 * 1000L);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        System.out.println("TASK completed");
	    };

    @Test
    public void completableFuture() throws Exception {

        CompletableFuture
            .runAsync(task)
            .thenCompose(aVoid1 -> CompletableFuture.runAsync(task))
            .thenAcceptAsync(aVoid2 -> System.out.println("all tasks completed!!"))
            .exceptionally(throwable -> {
                System.out.println("exception occurred!!");
                return null;
            });

        Thread.sleep(11 * 1000L);
    }
	    
	    
	    
    // �񵿱�ó�� Return ���� ���� ó���� Parameter �� ����Ҷ� ����Ѵ�
    //CompletableFuture �� ��ȯ�ϴ� Method�� Chain���� �����ϰ� ������
    //�� ������ Async ���μ����� ���� ���� ���� ���� Async ���μ����� ���ڷ� ����ϴ� ��쿡 �Ʒ��� ���� thenCompose, thenComposeAsync �� ����� �� �ִ�.
    @Test
    public void thenComposeTest() throws Exception {
        Price price = new Price();
        price.getPriceAsync(1)
                .thenComposeAsync(price::getPriceAsync)
                .thenComposeAsync(price::getPriceAsync)
                .thenComposeAsync(r -> price.getPriceAsync(r));

        System.out.println("Non Blocking!!");

        // main thread �� ������ child �� �� �׾� ������ �����.
        Thread.sleep(3000l);

    }
    
    // �ΰ��� ���μ����� parallel �ϰ� ���ÿ� �����ϰ� ��� ���� ������ ó���� �Ҷ�.
    // �ΰ��� �񵿱� ��û�� ���ÿ� �����ؼ� ���� �� �� �ִ�.
    @Test
    public void thenCombineTest() throws Exception {
        Price price = new Price();
        CompletableFuture<Double> price1 = price.getPriceAsync(1);
        CompletableFuture<Double> price2 = price.getPriceAsync(2);
        price2.thenCombineAsync(price1, (a, b) -> a+b )
                .thenAcceptAsync(System.out::print);

        System.out.println("Non Blocking!!");

        // main thread �� ������ child �� �� �׾� ������ �����.
        Thread.sleep(5000l);
    }
    
	    
	class Price {	
	    public double getPrice (double oldprice) throws Exception{
	        return calculatePrice(oldprice);
	    }
	
	    public double calculatePrice(double oldprice) throws Exception {
	
	        System.out.println("Input :" + oldprice);
	        Thread.sleep(1000l);
	        System.out.println("Output :" + (oldprice + 1l));
	
	        return oldprice + 1l;
	    }
	
	    public CompletableFuture<Double> getPriceAsync(double oldPrice) {
	        CompletableFuture<Double> completableFuture = new CompletableFuture<>();
	        new Thread(() -> {
	            try {
	                double price = calculatePrice(oldPrice);
	                completableFuture.complete(price);
	            } catch (Exception ex) {
	                completableFuture.completeExceptionally(ex);
	            }
	        }).start();
	
	        return completableFuture;
	    }
	}
}
	
/*
 * 	exceptionally
		���ݱ��� ����� completablefuture���� �߻��� Throwable �� ó���� �� �ִ�.
		ListenableFuture ��� Callback.onFailure �� ������ ����� �� �ִ�.
		���� ListenableFuture �� �� ��û���� ���п� ���� Callback �� ������ �ݸ�... CompletableFuture�� ��� Exception �� ���������� ó���� �� �ִ�.
		
		thenApply(Async)
			ListenableFuture ������ Callback �� �����ϴ� �Ͱ� ������ �۾��̴�.
			thenApply or thenApplyAsync �� Function �� ���ڷ� �ޱ� ������ ���� �ļ��۾��� ������� return �� �� �� �ִ�.
			
		thenAccept(Async)
			ListenableFuture ������ Callback �� �����ϴ� �Ͱ� ������ �۾������� Consumer �������̽��� �����̱� ������ �ļ� �۾��� ������� return �� �� ����.
 */
