package com.oltpbenchmark.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class TransactionLogger {
  private static final int BLOCK_SIZE = 100000;
  private static final List<JSONObject> buffer = new ArrayList<>();
  private static int blockCount = 0;

  public static synchronized void log(JSONObject txn) {
    buffer.add(txn);
    if (buffer.size() >= BLOCK_SIZE) {
      flush();
    }
  }

  public static synchronized void flush() {
    try (FileWriter file = new FileWriter("transaction_block_" + blockCount + ".json")) {
      file.write(buffer.toString());
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      buffer.clear();
      blockCount++;
    }
  }

  public static void close() {
    if (!buffer.isEmpty()) {
      flush();
    }
  }
}
