# Stubs for requests.packages.urllib3.exceptions (Python 3.4)
#
# NOTE: This dynamically typed stub was automatically generated by stubgen.

from typing import Any

class HTTPError(Exception): ...
class HTTPWarning(Warning): ...

class PoolError(HTTPError):
    pool = ...  # type: Any
    def __init__(self, pool, message) -> None: ...
    def __reduce__(self): ...

class RequestError(PoolError):
    url = ...  # type: Any
    def __init__(self, pool, url, message) -> None: ...
    def __reduce__(self): ...

class SSLError(HTTPError): ...
class ProxyError(HTTPError): ...
class DecodeError(HTTPError): ...
class ProtocolError(HTTPError): ...

ConnectionError = ...  # type: Any

class MaxRetryError(RequestError):
    reason = ...  # type: Any
    def __init__(self, pool, url, reason=...) -> None: ...

class HostChangedError(RequestError):
    retries = ...  # type: Any
    def __init__(self, pool, url, retries=...) -> None: ...

class TimeoutStateError(HTTPError): ...
class TimeoutError(HTTPError): ...
class ReadTimeoutError(TimeoutError, RequestError): ...
class ConnectTimeoutError(TimeoutError): ...
class EmptyPoolError(PoolError): ...
class ClosedPoolError(PoolError): ...
class LocationValueError(ValueError, HTTPError): ...

class LocationParseError(LocationValueError):
    location = ...  # type: Any
    def __init__(self, location) -> None: ...

class ResponseError(HTTPError):
    GENERIC_ERROR = ...  # type: Any
    SPECIFIC_ERROR = ...  # type: Any

class SecurityWarning(HTTPWarning): ...
class InsecureRequestWarning(SecurityWarning): ...
class SystemTimeWarning(SecurityWarning): ...
class InsecurePlatformWarning(SecurityWarning): ...
