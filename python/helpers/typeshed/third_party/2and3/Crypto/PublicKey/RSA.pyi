# Stubs for Crypto.PublicKey.RSA (Python 3.5)
#
# NOTE: This dynamically typed stub was automatically generated by stubgen.

from typing import Any, Optional, Union, Text
from .pubkey import pubkey

class _RSAobj(pubkey):
    keydata = ...  # type: Any
    implementation = ...  # type: Any
    key = ...  # type: Any
    def __init__(self, implementation, key, randfunc: Optional[Any] = ...) -> None: ...
    def __getattr__(self, attrname): ...
    def encrypt(self, plaintext, K): ...
    def decrypt(self, ciphertext): ...
    def sign(self, M, K): ...
    def verify(self, M, signature): ...
    def has_private(self): ...
    def size(self): ...
    def can_blind(self): ...
    def can_encrypt(self): ...
    def can_sign(self): ...
    def publickey(self): ...
    def exportKey(self, format: str = ..., passphrase: Optional[Any] = ..., pkcs: int = ...): ...

class RSAImplementation:
    error = ...  # type: Any
    def __init__(self, **kwargs) -> None: ...
    def generate(self, bits, randfunc: Optional[Any] = ..., progress_func: Optional[Any] = ..., e: int = ...): ...
    def construct(self, tup): ...
    def importKey(self, externKey: Any, passphrase: Union[None, bytes, Text] = ...) -> _RSAobj: ...

generate = ...  # type: Any
construct = ...  # type: Any
importKey = ...  # type: Any
error = ...  # type: Any
