#!/usr/bin/env python3
"""
file_scrumbler.py - scramble bytes in files under a directory

Usage examples:
  ./file_scrumbler.py /path/to/dir --percent 1.0 --byte-percent 0.5 --seed 42 --recursive
  ./file_scrumbler.py /path/to/dir --help

Options:
  --help            show this help
  --percent P       percent of files to touch (0-100)
  --byte-percent B  percent of bytes in each affected file to change (0-100)
  --mode MODE       'replace' (overwrite bytes), 'shuffle' (shuffle block), 'truncate'
  --seed S          random seed (for reproducibility)
  --recursive       recurse into subdirs
  --dry-run         don't write anything, just show what would be done
  --max-size BYTES  skip files larger than BYTES (default 50MB)
"""
import argparse, os, random, sys
from pathlib import Path

def parse_args():
    p = argparse.ArgumentParser(description="Scramble files under a directory")
    p.add_argument("dir", help="target directory")
    p.add_argument("--percent", type=float, default=1.0, help="percent of files to touch (0-100)")
    p.add_argument("--byte-percent", type=float, default=0.1, help="percent of bytes to change in each file touched (0-100)")
    p.add_argument("--mode", choices=["replace","shuffle","truncate"], default="replace", help="mode of corruption")
    p.add_argument("--seed", type=int, default=None)
    p.add_argument("--recursive", action="store_true")
    p.add_argument("--dry-run", action="store_true")
    p.add_argument("--max-size", type=int, default=50*1024*1024, help="skip files larger than bytes")
    return p.parse_args()

def list_files(root: Path, recursive: bool):
    if recursive:
        for p in root.rglob('*'):
            if p.is_file():
                yield p
    else:
        for p in root.iterdir():
            if p.is_file():
                yield p

def scramble_file(path: Path, byte_percent: float, mode: str, dry_run=False):
    try:
        size = path.stat().st_size
    except Exception as e:
        print(f"Skipping {path}: cannot stat ({e})")
        return False
    if size == 0:
        return False
    bytes_to_change = max(1, int(size * (byte_percent/100.0)))
    if bytes_to_change > size:
        bytes_to_change = size
    # pick some offsets (may include duplicates but that's okay)
    offsets = set()
    attempts = 0
    while len(offsets) < min(1000, bytes_to_change) and attempts < bytes_to_change*5+1000:
        offsets.add(random.randrange(0, size))
        attempts += 1
        if len(offsets) >= bytes_to_change:
            break
    offsets = sorted(list(offsets))
    print(f"Scrambling {path} size={size} bytes_ch={bytes_to_change} offsets_sample={offsets[:5]}")
    if dry_run:
        return True
    try:
        with open(path, "r+b", buffering=0) as f:
            if mode == "replace":
                for off in offsets:
                    f.seek(off)
                    f.write(bytes([random.randrange(0,256)]))
            elif mode == "shuffle":
                block_size = min(4096, size)
                start = random.randrange(0, max(1, size-block_size))
                f.seek(start)
                data = bytearray(f.read(block_size))
                random.shuffle(data)
                f.seek(start)
                f.write(data)
            elif mode == "truncate":
                newsize = max(0, size - bytes_to_change)
                f.truncate(newsize)
    except PermissionError:
        print(f"Permission denied writing {path}, skipping.")
        return False
    except Exception as e:
        print(f"Error modifying {path}: {e}")
        return False
    return True

def main():
    args = parse_args()
    if args.seed is not None:
        random.seed(args.seed)
    root = Path(args.dir)
    if not root.exists() or not root.is_dir():
        print("Directory not found:", root, file=sys.stderr)
        sys.exit(2)
    files = [p for p in list_files(root, args.recursive) if p.is_file() and p.stat().st_size <= args.max_size]
    total = len(files)
    to_touch = int(total * (args.percent/100.0))
    to_touch = max(1, to_touch) if total>0 and args.percent>0 else 0
    chosen = random.sample(files, to_touch) if to_touch>0 else []
    print(f"Found {total} files, touching {len(chosen)} files")
    for p in chosen:
        scramble_file(p, args.byte_percent, args.mode, dry_run=args.dry_run)

if __name__ == "__main__":
    main()
