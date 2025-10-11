# DefaultApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**existsRoom**](DefaultApi.md#existsRoom) | **GET** /api/v1/room/{roomId}/exists | Проверить, существует ли комната
[**getRoom**](DefaultApi.md#getRoom) | **GET** /api/v1/room/{roomId} | Получить данные комнаты по ID
[**updateRoom**](DefaultApi.md#updateRoom) | **PUT** /api/v1/room/{roomId} | Обновить данные комнаты


<a name="existsRoom"></a>
# **existsRoom**
> existsRoom(roomId)

Проверить, существует ли комната

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    UUID roomId = new UUID(); // UUID | 
    try {
      apiInstance.existsRoom(roomId);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#existsRoom");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **roomId** | [**UUID**](.md)|  |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Комната существует |  -  |
**404** | Комната не найдена |  -  |

<a name="getRoom"></a>
# **getRoom**
> RoomData getRoom(roomId)

Получить данные комнаты по ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    UUID roomId = new UUID(); // UUID | 
    try {
      RoomData result = apiInstance.getRoom(roomId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#getRoom");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **roomId** | [**UUID**](.md)|  |

### Return type

[**RoomData**](RoomData.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/vnd.t1internship.room.v1+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Успешный ответ |  -  |
**404** | Комната не найдена |  -  |

<a name="updateRoom"></a>
# **updateRoom**
> RoomData updateRoom(roomId, updateRoomRequest)

Обновить данные комнаты

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    UUID roomId = new UUID(); // UUID | 
    UpdateRoomRequest updateRoomRequest = new UpdateRoomRequest(); // UpdateRoomRequest | 
    try {
      RoomData result = apiInstance.updateRoom(roomId, updateRoomRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#updateRoom");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **roomId** | [**UUID**](.md)|  |
 **updateRoomRequest** | [**UpdateRoomRequest**](UpdateRoomRequest.md)|  |

### Return type

[**RoomData**](RoomData.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/vnd.t1internship.room.v1+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Комната обновлена |  -  |
**404** | Комната не найдена |  -  |

