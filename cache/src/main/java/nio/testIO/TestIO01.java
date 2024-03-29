package nio.testIO;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

public class TestIO01 {


    private  static String word2048;
    public static void main(String[] args) throws IOException {
        char[] chars=new char[2048];
        Arrays.fill(chars,'c');
        word2048= new String(chars);
        TestIO01 testIO01 = new TestIO01();
//        被压缩的文件夹
        File file = new File("C:\\迅雷下载\\硬盘测速\\demo");
//        tar包压缩
        String targetFile="C:\\迅雷下载\\test.tar";
//        测试生成文件
        File file1 = new File("C:\\迅雷下载\\硬盘测速\\demo\\d.csv");

        long l = System.currentTimeMillis();
//        FileOutputStream 耗时：3113
//        testIO01.writeBuffer(file);
//        testIO01.byteBuffer(file);
//        byteBuffer 耗时：2700
//        MappedByteBuffer  耗时：2466 写入数据量为上面的两倍
//        testIO01.mappedByteBuffer(file1);

        testIO01.dd(Paths.get(file.getPath()));
        Files.createDirectory(Paths.get(file.getPath()));
        testIO01.byteBuffer(file1);
//          新建一个tar包
        testIO01.tar(file,targetFile);

        testIO01.dd(Paths.get(file.getPath()));
        Files.createDirectory(Paths.get(file.getPath()));
//        testIO01.mappedByteBuffer(file);
        testIO01.byteBuffer(file1);
//          新建一个tar包
        testIO01.tar(file,targetFile+"1");

        System.out.println("耗时:"+(System.currentTimeMillis()-l));
    }

    private void dd(Path path){
        if (!Files.exists(path)) return ;
        try {
            Files.walkFileTree(path,new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return super.visitFile(file,attrs);
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return super.postVisitDirectory(dir,exc);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


//    打一个tar压缩
    private void tar(File srcDir,String targetFile) throws IOException {
        try(TarArchiveOutputStream tos=new TarArchiveOutputStream(Files.newOutputStream(Paths.get(targetFile)))){
            tarRecursive(tos,srcDir,"");
        }
    }

    private void tarRecursive(TarArchiveOutputStream tos,File srcFile,String BasePath) throws IOException {
//        递归打包文件夹
        if(srcFile.isDirectory()){
            File[] files=srcFile.listFiles();
            String nextBasePath = BasePath + srcFile.getName() + "/";
            if(files==null) {
                TarArchiveEntry entry = new TarArchiveEntry(srcFile, nextBasePath);
                tos.putArchiveEntry(entry);
            }else{
                for (File file : files) {
                    tarRecursive(tos,file,nextBasePath);
                }
            }
            int[][] nums = new int[2][2];
            Arrays.sort(nums, (a, b) -> a[0]-b[0]);
        }else{
            TarArchiveEntry entry=new TarArchiveEntry(srcFile,srcFile.getName());
            tos.putArchiveEntry(entry);
            FileUtils.copyFile(srcFile,tos);
            tos.closeArchiveEntry();
        }
    }



    private void writeBuffer(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
        int i=1000000;
        while(i>0){
            writer.write(word2048);
            i--;
        }
        writer.close();
        fos.close();
    }

    private void byteBuffer(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        FileChannel fc = fos.getChannel();
        byte[] datas = word2048.getBytes();
        ByteBuffer bbuf = ByteBuffer.allocate(1024 * 100);
        int i=100;
        while(i>0){
            for(int j=0;j<100;j++){
                bbuf.put(datas);
            }
            bbuf.flip();
            fc.write(bbuf);
            bbuf.clear();
           i--;
        }
        fos.close();

    }


    private void mappedByteBuffer(File file) throws IOException {
        RandomAccessFile acf = new RandomAccessFile(file, "rw");
        FileChannel fc = acf.getChannel();
        byte[] bs = word2048.getBytes();
        int len = bs.length * 1000;
        long offset=0;
        int i=2000;
        while(i>0){
            MappedByteBuffer mbuf = fc.map(FileChannel.MapMode.READ_WRITE, offset, len);
            for(int j=0;j<1000;j++){
                mbuf.put(bs);
            }
            offset=offset+len;
            i=i-1000;
        }
        fc.close();

    }

}
