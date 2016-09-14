import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;

import java.io.IOException;

public class MainClass {
    public static SpiDevice spi = null;
    public static void main(String args[]) throws InterruptedException, IOException {
    	
    	System.out.println("Starting MCP3208 App.");
        spi = SpiFactory.getInstance(SpiChannel.CS0,SpiDevice.DEFAULT_SPI_SPEED,SpiDevice.DEFAULT_SPI_MODE);
        while(true){
        	System.out.println(getConversionValue((short)4)); //Print out red ADC Sample Counts
            Thread.sleep(2000);
        }
        
    }
    public static int getConversionValue(short channel) throws IOException {

        byte data[] = new byte[] {
                (byte)0b00000000,// first byte, with start bit
                (byte) ((byte)channel<<6),
                (byte)0b00000000                               // third byte transmitted....don't care
        };
        if(channel>3)
        	data[0]= 0B00000111; //First Byte for Channel 3-7
    	 else
    		data[0]= 0B00000110;//First Byte for Channel 0-3

       
        byte[] result = spi.write(data); //Request data from MCP3208 via SPI

   
        int value = (result[1]<< 8) & 0b0000111111111111; //merge data[1] & data[2] to get 10-bit result
        value |=  (result[2] & 0xff);
        return value;
    }
}
