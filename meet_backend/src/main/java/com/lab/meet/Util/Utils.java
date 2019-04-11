package com.lab.meet.Util;

import com.lab.meet.Dao.AuthService;
import com.lab.meet.Model.FileEntity;
import com.lab.meet.Model.GroupSummary;
import com.lab.meet.Model.Role;
import com.lab.meet.Model.User;
import com.lab.meet.Repository.FileRepository;
import com.lab.meet.Repository.GroupSummaryRepository;
import com.lab.meet.Repository.RoleRepository;
import com.lab.meet.Repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {
    //save file
    public static void saveUploadedFile(MultipartFile file, String upload_folder) throws IOException {
        byte[] bytes = file.getBytes();
        checkFolderExist(upload_folder);
        Path path = Paths.get(upload_folder + file.getOriginalFilename());
        Files.write(path, bytes);
    }

    public static String saveUploadedImage(MultipartFile file, String upload_folder) throws IOException {
        byte[] bytes = file.getBytes();
        checkFolderExist(upload_folder);
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "";
        String fileOriginName = file.getOriginalFilename();
        String[] splits = fileOriginName.split("\\.");
        if (splits.length > 0)
            fileName = fileName + "." + splits[splits.length - 1];
        Path path = Paths.get(upload_folder + fileName);
        Files.write(path, bytes);
        return fileName;
    }

    public static String saveFileByDate(MultipartFile file, String upload_folder) throws IOException {
        Date date = new Date();
        String pathName = upload_folder + new SimpleDateFormat("yyyy/MM/dd/").format(date);
        checkFolderExist(pathName);
        byte[] bytes = file.getBytes();
        Path path = Paths.get(pathName + file.getOriginalFilename());
        Files.write(path, bytes);
        return new SimpleDateFormat("yyyy/MM/dd/").format(date) + file.getOriginalFilename();
    }

    private static boolean isImage(File file) throws IOException {
        BufferedImage bi = ImageIO.read(file);
        return bi != null;
    }

    public static String getContentPath(HttpServletRequest request) {
        WebApplicationContext webApplicationContext = (WebApplicationContext) SpringContextUtils.applicationContext;
        ServletContext servletContext = webApplicationContext.getServletContext();
        String projectPath = servletContext.getContextPath();
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + projectPath;
    }

    private static void checkFolderExist(String folder) {
        File f = new File(folder);
        if (!f.exists())
            f.mkdirs();
    }

    public static String getDefaultAvatarUrl(HttpServletRequest request, String baseDir, String fileName) {
        String contentPath = getContentPath(request);
        String downloadRoute = "/public/files/";
        String url = contentPath + downloadRoute + baseDir + fileName;
        return url;
    }

    public static <T> Map<String, Object> formatPagable(Page<T> page) {
        List<T> data = page.getContent();
        boolean isLast = page.isLast();
        int pageSize = page.getPageable().getPageSize();
        int totalPages = page.getTotalPages();
        long totalSize = page.getTotalElements();
        Map<String, Object> map = new HashMap<>();
        map.put("list", data);
        map.put("page_size", pageSize);
        map.put("total_pages", totalPages);
        map.put("total_size", totalSize);
        map.put("is_last", isLast);
        return map;
    }

    public static Long timeFormat(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = format.parse(dateString);
            return date.getTime();
        } catch (Exception ignore) {
            return new Date().getTime();
        }
    }


    public static Set<FileEntity> findDocsByIds(FileRepository fileRepository, String ids) {
        String[] splits = ids.split(",");
        Set<FileEntity> fileEntities = new HashSet<>();
        FileEntity fileEntity;
        Long id;
        for (String idString : splits) {
            if (StringUtils.isNoneBlank(idString) && StringUtils.isNumeric(idString)) {
                id = Long.parseLong(idString);
                fileEntity = fileRepository.findFileEntityById(id);
                if (fileEntity != null)
                    fileEntities.add(fileEntity);
            }
        }
        return fileEntities;
    }

    public static Set<GroupSummary> findSummariesByIds(GroupSummaryRepository summaryRepository, String ids) {
        String[] splits = ids.split(",");
        Set<GroupSummary> summaries = new HashSet<>();
        GroupSummary summary;
        Long id;
        for (String idString : splits) {
            if (StringUtils.isNoneBlank(idString) && StringUtils.isNumeric(idString)) {
                id = Long.parseLong(idString);
                summary = summaryRepository.findGroupSummaryById(id);
                if (summary != null)
                    summaries.add(summary);
            }
        }
        return summaries;
    }

    public static Set<User> findUsersByIds(UserRepository userRepository, String ids) {
        String[] splits = ids.split(",");
        Set<User> users = new HashSet<>();
        User user;
        Long id;
        for (String idString : splits) {
            if (StringUtils.isNoneBlank(idString) && StringUtils.isNumeric(idString)) {
                id = Long.parseLong(idString);
                user = userRepository.findSysUserById(id);
                if (user != null)
                    users.add(user);
            }
        }
        return users;
    }

    public static Set<Role> findRolesByIds(RoleRepository repository, String ids) {
        String[] splits = ids.split(",");
        Set<Role> roles = new HashSet<>();
        Role role;
        Long id;
        for (String idString : splits) {
            if (StringUtils.isNoneBlank(idString) && StringUtils.isNumeric(idString)) {
                id = Long.parseLong(idString);
                role = repository.findSysRoleById(id);
                if (role != null)
                    roles.add(role);
            }
        }
        return roles;
    }

    public static User getCurrentUser(AuthService service, HttpServletRequest request, String tokenHeader) {
        System.out.println(tokenHeader);
        String token = request.getHeader(tokenHeader);
        System.out.println(token);
        return service.getCurrentUser(token);
    }

    public static boolean isAdmin(Set<Role> roles) {
        for (Role role : roles)
            if (role.getName().toLowerCase().contains("admin"))
                return true;
        return false;
    }


    public static boolean isImagesTrue(String posturl) throws IOException {
        URL url = new URL(posturl);
        HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
        urlcon.setRequestMethod("POST");
        urlcon.setRequestProperty("Content-type",
                "application/x-www-form-urlencoded");
        return (urlcon.getResponseCode() == HttpURLConnection.HTTP_OK);
    }
}
