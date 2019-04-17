import tkinter as tk
from tkinter import filedialog
import json

def json_to_txt():
    root = tk.Tk()
    root.withdraw()

    file_path = filedialog.askopenfilename()


    with open(file_path) as f:
      data = json.load(f)

    # Output: {'name': 'Bob', 'languages': ['English', 'Fench']}
    print(data)
    
json_to_txt()
