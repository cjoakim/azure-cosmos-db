import os
import time

import jinja2

# This class is used to create text content using jinja2 templates.
# Chris Joakim, Microsoft

class Template(object):

    @classmethod
    def get_template(cls, root_dir, name):
        filename = 'templates/{}'.format(name)
        return cls.get_jinja2_env(root_dir).get_template(filename)

    @classmethod
    def render(cls, template, values):
        return template.render(values)

    @classmethod
    def get_jinja2_env(cls, root_dir):
        print('get_jinja2_env root_dir: {}'.format(root_dir))
        return jinja2.Environment(
            loader = jinja2.FileSystemLoader(
                root_dir), autoescape=True)
