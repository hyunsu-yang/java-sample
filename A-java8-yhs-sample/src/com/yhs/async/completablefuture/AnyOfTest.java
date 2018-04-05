package com.yhs.async.completablefuture;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.Test;

/*
���ÿ� n���� ��û�� ȣ���ϰ� �ϳ��� ȣ���� �ϼ��Ǹ� �����ϱ�

�Ʒ� ������ anyOf �޼��带 ����ؼ� 3���� ��û �߿� �ϳ��� ������ �������� �Ѿ���� �����Ǿ� �ִ�.
thenAcceptAsync �޼��忡���� 3�� �� ���� ���� ������ ����� �ֿܼ� �ﵵ�� �Ǿ� �ִ�.
���� AllOf �� �޸� 3���߿� 1���� ����� ǥ�õȴ�.
*/
public class AnyOfTest {

    private String buildMessage(int index) {

        try {
            Thread.sleep(2 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "Completed!! [" + index + "]";
    }

    @Test
    public void allOfTest() throws Exception {
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> buildMessage(1));
        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> buildMessage(2));
        CompletableFuture<String> cf3 = CompletableFuture.supplyAsync(() -> buildMessage(3));

        List<CompletableFuture<String>> completableFutures = Arrays.asList(cf1, cf2, cf3);

        CompletableFuture
            .anyOf(completableFutures.toArray(new CompletableFuture[3]))
            .thenAcceptAsync(result -> System.out.println(result));

        Thread.sleep(7 * 1000L);
    }
}