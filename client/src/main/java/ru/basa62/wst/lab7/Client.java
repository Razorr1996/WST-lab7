package ru.basa62.wst.lab7;

import lombok.SneakyThrows;
import org.apache.juddi.api_v3.AccessPointType;
import org.uddi.api_v3.*;
import ru.basa62.wst.lab7.ws.client.BooksEntity;
import ru.basa62.wst.lab7.ws.client.BooksService;
import ru.basa62.wst.lab7.ws.client.BooksServiceException;
import ru.basa62.wst.lab7.ws.client.BooksService_Service;

import javax.xml.ws.BindingProvider;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Client {
    private static JUDDIClient juddiClient;
    private static BooksService service;

    public static void main(String[] args) throws IOException {
        BooksService_Service booksService = new BooksService_Service();
        service = booksService.getBooksServicePort();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter JUDDI username");
        String username = bufferedReader.readLine().trim();
        System.out.println("Enter JUDDI user password");
        String password = bufferedReader.readLine().trim();
        juddiClient = new JUDDIClient("META-INF/uddi.xml");
        juddiClient.authenticate(username, password);

        System.out.println("Добро пожаловать в библиотеку.");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int currentState = 6;
        while (true) {
            switch (currentState) {
                case 99:
                    return;
                case 0:
                    System.out.println("\nМеню:\n0 - Это меню\n" +
                            "1 - findAll\n" +
                            "2 - filter\n" +
                            "3 - create\n" +
                            "4 - update\n" +
                            "5 - delete\n" +
                            "6 - jUDDI\n" +
                            "99 - выход");
                    currentState = readState(currentState, reader);
                    break;
                case 1:
                    findAll();
                    currentState = 0;
                    break;
                case 2:
                    filter();
                    currentState = 0;
                    break;
                case 3:
                    create();
                    currentState = 0;
                    break;
                case 4:
                    update();
                    currentState = 0;
                    break;
                case 5:
                    delete();
                    currentState = 0;
                    break;
                case 6:
                    int state = 0;
                    boolean br = false;
                    while (!br) {
                        switch (state) {
                            case 0:
                                System.out.println("\nВыберите один из пунктов:");
                                System.out.println("1. Вывести все бизнесы");
                                System.out.println("2. Зарегистрировать бизнес");
                                System.out.println("3. Зарегистрировать сервис");
                                System.out.println("4. Найти и использовать сервис");
                                System.out.println("5. Выйти");
                                state = readState(currentState, reader);
                                break;
                            case 1:
                                listBusinesses(null);
                                state = 0;
                                break;
                            case 2:
                                System.out.println("Введите имя бизнеса");
                                String bnn = readString(reader);
                                if (bnn != null) {
                                    createBusiness(bnn);
                                }
                                state = 0;
                                break;
                            case 3:
                                listBusinesses(null);
                                String bbk;
                                do {
                                    System.out.println("Введите ключ бизнеса");
                                    bbk = readString(reader);
                                } while (bbk == null);

                                String ssn;
                                do {
                                    System.out.println("Введите имя сервиса");
                                    ssn = readString(reader);
                                } while (ssn == null);

                                String ssurl;
                                do {
                                    System.out.println("Введите ссылку на wsdl");
                                    ssurl = readString(reader);
                                } while (ssurl == null);
                                createService(bbk, ssn, ssurl);
                                state = 0;
                                break;
                            case 4:
                                System.out.println("Введите имя сервиса для поиска");
                                String ffsn = readString(reader);
                                filterServices(ffsn);
                                System.out.println("Введите ключ сервиса");
                                String kkey = readString(reader);
                                if (kkey != null) {
                                    useService(kkey);
                                }
                                currentState = 0;
                                br = true;
                                break;
                            case 5:
                                return;
                            default:
                                state = 0;
                                break;
                        }
                    }
                    break;
                default:
                    currentState = 0;
                    break;
            }
        }

    }

    private static void findAll() {
        System.out.println("Выведем все книги:");
        try {
            List<BooksEntity> books1 = service.findAll();
            for (BooksEntity book : books1) {
                System.out.println(printBook(book));
            }
        } catch (BooksServiceException e) {
            System.out.println(e.getFaultInfo().getMessage());
        }
    }

    private static void filter() {
        Scanner in = new Scanner(System.in);
        System.out.println("Поищем книги:");
        System.out.print("ID: ");
        String idStr = checkEmpty(in.nextLine());
        Long id = null;
        if (idStr != null) {
            id = Long.parseLong(idStr);
        }
        System.out.print("Название: ");
        String name = checkEmpty(in.nextLine());

        System.out.print("Автор: ");
        String author = checkEmpty(in.nextLine());

        System.out.print("Дата публикации (yyyy-MM-dd): ");
        String publicDate = checkEmpty(in.nextLine());

        System.out.print("ISBN: ");
        String isbn = checkEmpty(in.nextLine());

        try {
            List<BooksEntity> books2 = service.filter(id, name, author, publicDate, isbn);

            if (books2.size() == 0) {
                System.out.println("Ничего не найдено");
            } else {
                System.out.println("Найдено:");
                for (BooksEntity book : books2) {
                    System.out.println(printBook(book));
                }
            }
        } catch (BooksServiceException e) {
            System.out.println(e.getFaultInfo().getMessage());
        }
    }

    private static void create() {
        Scanner in = new Scanner(System.in);
        System.out.println("Создадим книгу:");

        System.out.print("Название: ");
        String name = checkEmpty(in.nextLine());

        System.out.print("Автор: ");
        String author = checkEmpty(in.nextLine());

        System.out.print("Дата публикации (yyyy-MM-dd): ");
        String publicDate = checkEmpty(in.nextLine());

        System.out.print("ISBN: ");
        String isbn = checkEmpty(in.nextLine());

        try {
            Long newId = service.create(name, author, publicDate, isbn);
            System.out.printf("Новый ID: %d", newId);
        } catch (BooksServiceException e) {
            System.out.println(e.getFaultInfo().getMessage());
        }
    }

    private static void update() {
        Scanner in = new Scanner(System.in);
        System.out.println("Обновим книгу:");
        System.out.print("ID: ");

        String idStr = checkEmpty(in.nextLine());
        Long id = null;
        if (idStr != null) {
            id = Long.parseLong(idStr);
        }

        System.out.print("Название: ");
        String name = checkEmpty(in.nextLine());

        System.out.print("Автор: ");
        String author = checkEmpty(in.nextLine());

        System.out.print("Дата публикации (yyyy-MM-dd): ");
        String publicDate = checkEmpty(in.nextLine());

        System.out.print("ISBN: ");
        String isbn = checkEmpty(in.nextLine());

        try {
            int count = service.update(id, name, author, publicDate, isbn);
            System.out.printf("Обновлено: %d", count);
        } catch (BooksServiceException e) {
            System.out.println(e.getFaultInfo().getMessage());
        }
    }

    private static void delete() {
        Scanner in = new Scanner(System.in);
        System.out.println("Удалим книгу:");
        System.out.print("ID: ");
        String idStr = checkEmpty(in.nextLine());
        if (idStr != null) {
            long id = Long.parseLong(idStr);
            try {
                int count = service.delete(id);
                System.out.printf("Удалено: %d", count);
            } catch (BooksServiceException e) {
                System.out.println(e.getFaultInfo().getMessage());
            }
        } else {
            System.out.println("Ничего не введено");
        }
    }

    private static String printBook(BooksEntity b) {
        Formatter fmt = new Formatter();
        return fmt.format("ID: %d, Book: %s, Author: %s, PublicDate: %s, ISBN: %s", b.getId(), b.getName(), b.getAuthor(), b.getPublicDate().toString(), b.getIsbn()).toString();
    }

    private static String checkEmpty(String s) {
        return s.length() == 0 ? null : s;
    }

    private static int readState(int current, BufferedReader reader) {
        try {
            return Integer.parseInt(reader.readLine());
        } catch (java.lang.Exception e) {
            return current;
        }
    }

    private static String readString(BufferedReader reader) throws IOException {
        String trim = reader.readLine().trim();
        if (trim.isEmpty()) {
            return null;
        }
        return trim;
    }

    @SneakyThrows
    private static void useService(String serviceKey) {

        ServiceDetail serviceDetail = juddiClient.getService(serviceKey.trim());
        if (serviceDetail == null || serviceDetail.getBusinessService() == null || serviceDetail.getBusinessService().isEmpty()) {
            System.out.printf("Can not find service by key '%s'\b", serviceKey);
            return;
        }
        List<BusinessService> services = serviceDetail.getBusinessService();
        BusinessService businessService = services.get(0);
        BindingTemplates bindingTemplates = businessService.getBindingTemplates();
        if (bindingTemplates == null || bindingTemplates.getBindingTemplate().isEmpty()) {
            System.out.printf("No binding template found for service '%s' '%s'\n", serviceKey, businessService.getBusinessKey());
            return;
        }
        for (BindingTemplate bindingTemplate : bindingTemplates.getBindingTemplate()) {
            AccessPoint accessPoint = bindingTemplate.getAccessPoint();
            if (accessPoint.getUseType().equals(AccessPointType.END_POINT.toString())) {
                String value = accessPoint.getValue();
                System.out.printf("Use endpoint '%s'\n", value);
                changeEndpointUrl(value);
                return;
            }
        }
        System.out.printf("No endpoint found for service '%s'\n", serviceKey);
    }

    @SneakyThrows
    private static void createService(String businessKey, String serviceName, String wsdlUrl) {
        List<ServiceDetail> serviceDetails = juddiClient.publishUrl(businessKey.trim(), serviceName.trim(), wsdlUrl.trim());
        System.out.printf("Services published from wsdl %s\n", wsdlUrl);
        JUDDIUtil.printServicesInfo(serviceDetails.stream()
                .map(ServiceDetail::getBusinessService)
                .flatMap(List::stream)
                .collect(Collectors.toList())
        );
    }

    @SneakyThrows
    public static void createBusiness(String businessName) {
        businessName = businessName.trim();
        BusinessDetail business = juddiClient.createBusiness(businessName);
        System.out.println("New business was created");
        for (BusinessEntity businessEntity : business.getBusinessEntity()) {
            System.out.printf("Key: '%s'\n", businessEntity.getBusinessKey());
            System.out.printf("Name: '%s'\n", businessEntity.getName().stream().map(Name::getValue).collect(Collectors.joining(" ")));
        }
    }

    public static void changeEndpointUrl(String endpointUrl) {
        ((BindingProvider) service).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl.trim());
    }


    @SneakyThrows
    private static void filterServices(String filterArg) {
        List<BusinessService> services = juddiClient.getServices(filterArg);
        JUDDIUtil.printServicesInfo(services);
    }

    @SneakyThrows
    private static void listBusinesses(Void ignored) {
        JUDDIUtil.printBusinessInfo(juddiClient.getBusinessList().getBusinessInfos());
    }
}
