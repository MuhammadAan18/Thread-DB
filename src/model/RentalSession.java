package model;
import java.sql.Timestamp;

public class RentalSession {
  public final int id, tableId;
  public final Timestamp start, end;
  public final int remainingSec; 

  public RentalSession(int id, int tableId, Timestamp start, Timestamp end, int remainingSec) {
    this.id=id; this.tableId=tableId; this.start=start; this.end=end; this.remainingSec=remainingSec;
  }
}