// 3.

package FirstPartialExam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.*;

class FileNameExistsException extends Exception{
    public FileNameExistsException(String file, String folder) {
        super(String.format("There is already a file named %s in the folder %s", file, folder));
    }
}
interface IFile extends Comparable<IFile> {
    String getFileName();

    long getFileSize();

    String getFileInfo(int intend);

    void sortBySize();

    long findLargestFile();
}

class File implements IFile {
    private String nameOfFile;
    private long sizeOfFile;

    public File(String nameOfFile, long sizeOfFile) {
        this.nameOfFile = nameOfFile;
        this.sizeOfFile = sizeOfFile;
    }

    @Override
    public String getFileName() {
        return nameOfFile;
    }

    @Override
    public long getFileSize() {
        return sizeOfFile;
    }

    @Override
    public String getFileInfo(int intend) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<intend; i++) {
            sb.append("    ");   //tab
        }
        sb.append(String.format("File name: %10s File size: %10d\n", nameOfFile, getFileSize()));
        return sb.toString();
    }


    @Override
    public void sortBySize() {}

    @Override
    public long findLargestFile() {
        return sizeOfFile;
    }

    @Override
    public int compareTo(IFile o) {
        return Long.compare(this.getFileSize(), o.getFileSize());
    }
}

class Folder implements IFile {
    private String nameOfFolder;
    private long sizeOfFolder;
    List<IFile> filesInFolder;

    public Folder(String nameOfFolder) {
        this.nameOfFolder = nameOfFolder;
        this.sizeOfFolder = 0;
        this.filesInFolder = new ArrayList<>();
    }

    @Override
    public String getFileName() {
        return nameOfFolder;
    }

    @Override
    public long getFileSize() {
        sizeOfFolder =filesInFolder.stream().mapToLong(IFile::getFileSize).sum();
        return sizeOfFolder;
    }

    @Override
    public String getFileInfo(int intend) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<intend; i++) {
            sb.append("    "); // tab
        }
        sb.append(String.format("Folder name: %10s Folder size: %10d\n", nameOfFolder, getFileSize()));
        filesInFolder.forEach(file -> sb.append(file.getFileInfo(intend+1)));
        return sb.toString();
    }

    @Override
    public void sortBySize() {
        filesInFolder.sort(Comparator.naturalOrder());
        for (IFile file : filesInFolder) {
            if (file instanceof Folder) {
                ((Folder) file).sortBySize();
            }
        }
    }

    @Override
    public long findLargestFile() {
        long maxSize = 0;
        for(IFile file : filesInFolder){
            if(file instanceof File)
                maxSize = Math.max(maxSize, file.getFileSize());
            else if(file instanceof Folder)
                maxSize = Math.max(maxSize, file.findLargestFile());
        }
        return maxSize;
    }

    public void addFile(IFile file) throws FileNameExistsException {
        if(filesInFolder.stream().anyMatch(f -> f.getFileName().equals(file.getFileName())))
            throw new FileNameExistsException(file.getFileName(), nameOfFolder);
        filesInFolder.add(file);
    }
    @Override
    public int compareTo(IFile o) {
        return Long.compare(this.getFileSize(), o.getFileSize());
    }

}

class FileSystem{
    Folder rootDirectory;

    public FileSystem() {
        this.rootDirectory = new Folder("root");
    }

    public void addFile (IFile file) throws FileNameExistsException {
        rootDirectory.addFile(file);
    }
    public long findLargestFile(){
       return rootDirectory.findLargestFile();
    }

    public void sortBySize(){
        rootDirectory.sortBySize();
    }

    @Override
    public String toString() {
        return rootDirectory.getFileInfo(0);
    }
}

public class FileSystemTest {

    public static Folder readFolder(Scanner sc) {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < totalFiles; i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String[] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args) {

        //file reading from input

        Scanner sc = new Scanner(System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());


    }
}