package com.yhs.async.completablefuture;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.junit.Test;

/*
 * ���ÿ� n���� ��û�� ȣ���ϰ� ��� ȣ���� �ϼ��Ǹ� �����ϱ� 
 * ���ÿ� 3���� Rest ��û�� ������ 3���� ��û�� �Ϸ�Ǹ� Callback �� �����ϱ� ���� ����
	CompletableFuture.allOf �� ����Ѵ�.
	�Ʒ� ���� 5�� �ڿ� "Completed!!" �� ��ȯ�ϴ� buildMessage ��� �޼��带 ���ÿ� 3���� ȣ���ϰ� 3���� �Ϸ�Ǿ����� �� ����� ������ thenAcceptAsync ���� ��� ��Ƽ� ó���� �Ѵ�.
	���� ���谡 ���� �����͸� ���ÿ� ��ȸ �Ҷ�, ������ ����� �� �ִ�.
 */
public class AllOfTest {

    private String buildMessage() {

        try {
            Thread.sleep(2 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "Completed!!";
    }

    @Test
    public void allOfTest() throws Exception {
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(this::buildMessage);
        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(this::buildMessage);
        CompletableFuture<String> cf3 = CompletableFuture.supplyAsync(this::buildMessage);

        List<CompletableFuture<String>> completableFutures = Arrays.asList(cf1, cf2, cf3);

        CompletableFuture
            .allOf(completableFutures.toArray(new CompletableFuture[3]))
          .thenApplyAsync(result ->
                                completableFutures
                                    .stream()
                                    .map(a -> a.join())
                                    .collect(Collectors.toList())
            )
            .thenAcceptAsync(messages -> messages.forEach(message -> System.out.println(message)));

        Thread.sleep(7 * 1000L);
    }
}