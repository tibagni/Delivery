package com.delivery.menu;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.delivery.Logger;
import com.delivery.engine.command.MenuCommand;
import com.delivery.util.StringUtils;

public class UploadImageCmd extends MenuCommand {
    private String mImagePath;

    @Override
    public void execute(HttpServletRequest request) {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        Logger.debug("UploadImageCmd - request: " + request);
        if (!isMultipart) {
            Logger.debug("UploadImageCmd - nao multipart");
        } else {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = null;

            try {
                items = upload.parseRequest(request);
                Logger.debug("itens: " + items);
            } catch (FileUploadException e) {
                Logger.error("Upload de imagem falhou", e);
            }
            Iterator<FileItem> itr = items.iterator();
            while (itr.hasNext()) {
                FileItem item = (FileItem) itr.next();
                if (item.isFormField()){
                    String name = item.getFieldName();
                    String value = item.getString();
                    Logger.debug("Campo de formulario: " + name + " = " + value);
                } else {
                    try {
                        String mimetype = item.getContentType();
                        Logger.debug("Mimetype: " + mimetype);
                        if (StringUtils.isEmpty(mimetype) || !mimetype.startsWith("image/")) {
                            Logger.warning("Mimetype invalido.");
                            continue;
                        }

                        // Vamos reimensionar a imagem caso necessario (para que ela nao fique muito grande)
                        BufferedImage img = resizeImg(item.getInputStream());


                        String itemName = item.getName();
                        Random generator = new Random();
                        int r = generator.nextInt(Integer.MAX_VALUE);

                        String reg = "[.*]";
                        String replacingtext = "";

                        Pattern pattern = Pattern.compile(reg);
                        Matcher matcher = pattern.matcher(itemName);
                        StringBuffer buffer = new StringBuffer();

                        while (matcher.find()) {
                            matcher.appendReplacement(buffer, replacingtext);
                        }
                        int indexOf = itemName.indexOf(".");
                        String extension = itemName.substring(indexOf + 1);
                        Logger.debug("extensao: " + extension);

                        String finalImage = buffer.toString() + "_" + r + "." + extension;
                        Logger.debug("Imagem Final === " + finalImage);

                        String path = request.getServletContext().getRealPath("/pictures");
                        File savedFile = new File(path + "/" + finalImage);
                        ImageIO.write(img, extension, savedFile);

                        mImagePath = savedFile.getAbsolutePath();
                        request.setAttribute("img", "pictures/" + finalImage);
                        Logger.debug("Imagem salva em: " + mImagePath);
                    } catch (Exception e) {
                        Logger.error("Erro no upload da imagem", e);
                    }
                }
            }
        }
    }

    // Dimensoes maximas da imagem
    private static int MAX_WIDTH  = 150;
    private static int MAX_HEIGHT = 100;

    private BufferedImage resizeImg(InputStream inputStream) throws IOException {
        BufferedImage originalImage = ImageIO.read(inputStream);
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        int newWidth = width;
        int newHeight = height;
        //Ajusta o height de acordo com o min width
        if (width > MAX_WIDTH) {
            newWidth = MAX_WIDTH;
            newHeight = (newWidth * height) / width;

            width = newWidth;
            height = newHeight;
        }
        //Ajusta o width de acordo com o min height
        if (height > MAX_HEIGHT) {
            newHeight = MAX_HEIGHT;
            newWidth = (newHeight * width) / height;

            width = newWidth;
            height = newHeight;
        }

        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }

    @Override
    public String getRedirect() {
        return "cardapio/fotoCarregada.jsp";
    }

}
