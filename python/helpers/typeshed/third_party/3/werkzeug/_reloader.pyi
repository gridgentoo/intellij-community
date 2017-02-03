# Stubs for werkzeug._reloader (Python 3.5)
#
# NOTE: This dynamically typed stub was automatically generated by stubgen.

from typing import Any

class ReloaderLoop:
    name = ...  # type: Any
    extra_files = ...  # type: Any
    interval = ...  # type: Any
    def __init__(self, extra_files=None, interval=1): ...
    def run(self): ...
    def restart_with_reloader(self): ...
    def trigger_reload(self, filename): ...
    def log_reload(self, filename): ...

class StatReloaderLoop(ReloaderLoop):
    name = ...  # type: Any
    def run(self): ...

class WatchdogReloaderLoop(ReloaderLoop):
    observable_paths = ...  # type: Any
    name = ...  # type: Any
    observer_class = ...  # type: Any
    event_handler = ...  # type: Any
    should_reload = ...  # type: Any
    def __init__(self, *args, **kwargs): ...
    def trigger_reload(self, filename): ...
    def run(self): ...

reloader_loops = ...  # type: Any

def run_with_reloader(main_func, extra_files=None, interval=1, reloader_type=''): ...
